package com.stcu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stcu.model.ParadaRecorrido;
import com.stcu.model.Recorrido;

public interface ParadaRecorridoRepository extends JpaRepository<ParadaRecorrido,Long>{
    ParadaRecorrido findById(long id);

    List<ParadaRecorrido> findByRecorrido( Recorrido rec );

    List<ParadaRecorrido> findByRecorridoId( long id );

    @Query("SELECT pr FROM ParadaRecorrido pr WHERE pr.recorrido.id = ?1")
    List<ParadaRecorrido> findParadaRecorridos( long idrecorrido );

    @Query("SELECT pr FROM ParadaRecorrido pr WHERE pr.recorrido.linea.denominacion = ?1 AND pr.recorrido.denominacion = ?2 ORDER BY pr.orden")
    List<ParadaRecorrido> findParadasRecorridoByLineaDenomYRecorridoDenom(String lineaDenom, String recorridoDenom);


    //para app pasajero
    @Query("SELECT pr FROM ParadaRecorrido pr WHERE pr.recorrido.linea.id = ?1 AND pr.recorrido.id = ?2 ORDER BY pr.orden")
    List<ParadaRecorrido> getParadasRecorridoByLineaIdYRecorridoId(long idLinea,long idRecorrido);

    @Query("SELECT pr FROM ParadaRecorrido pr WHERE pr.recorrido.linea.denominacion = ?1 AND pr.recorrido.activo is TRUE ORDER BY pr.orden")
    List<ParadaRecorrido> getParadasRecorridoByLineaDenom(String lineaDenom);




    // para mostrar ubicacion parada pasajero en mapa arribo colectivo mobile
    @Query("SELECT pr FROM ParadaRecorrido pr WHERE pr.recorrido.linea.id = ?1 AND pr.recorrido.id = ?2 AND pr.parada.codigo = ?3")
    ParadaRecorrido getParadaRecorrido(long idLinea, long idRecorrido, long codigoParada);

}
