package com.stcu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stcu.model.ColectivoRecorrido;

public interface ColectivoRecorridoRepository extends JpaRepository<ColectivoRecorrido,Long> {
    
    ColectivoRecorrido findById( long id );

    List<ColectivoRecorrido> findByTransitoTrue();

    List<ColectivoRecorrido> findByTransitoTrueOrderByIdDesc();

    // get lista de colectivosRecorrido activos
    @Query("SELECT cr FROM ColectivoRecorrido cr WHERE cr.recorrido.linea.id = ?1 AND cr.recorrido.id = ?2 AND cr.hasta IS NULL AND cr.transito is TRUE")
    List<ColectivoRecorrido> findAllColectivosRecorridoActivos( long idLinea, long idRecorrido);

    //para App Colectivo
    //error al traer dos resultados. tiene que buscar el que este activo
    @Query("SELECT cr FROM ColectivoRecorrido cr WHERE cr.recorrido.linea.denominacion = ?1 AND cr.recorrido.denominacion = ?2 AND cr.colectivo.unidad = ?3 AND cr.hasta IS NULL AND cr.transito is TRUE")
    ColectivoRecorrido findColectivoRecorrido( String denomLinea, String denomRecorrido, String unidad);
 
    @Query("SELECT cr FROM ColectivoRecorrido cr WHERE cr.id = ?1 AND cr.recorrido.linea.id = ?2 AND cr.hasta IS NULL AND cr.transito is TRUE")
    ColectivoRecorrido findColectivoRecorridoEnTransito( long idColeRec, long idLinea);

    // get colectivo recorrido fuera de circulacion
    @Query("SELECT cr FROM ColectivoRecorrido cr WHERE cr.recorrido.linea.denominacion = ?1 AND cr.recorrido.denominacion = ?2 AND cr.colectivo.unidad = ?3 AND cr.hasta IS NULL")
    ColectivoRecorrido findColectivoRecorridoFueraDeCiruclacion( String denomLinea, String denomRecorrido, String unidad);

}
