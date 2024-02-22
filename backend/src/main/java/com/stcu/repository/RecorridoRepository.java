package com.stcu.repository;

import org.locationtech.jts.geom.LineString;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.stcu.model.Recorrido;

public interface RecorridoRepository extends JpaRepository<Recorrido,Long> {

    Recorrido findById( long id );

    @Query("SELECT r FROM Recorrido r WHERE r.linea.id = ?1 AND r.denominacion = ?2 AND r.activo=true")
    Recorrido findRecorridoActivoByDenominacion( long idlinea, String denominacion );

    @Query("SELECT r FROM Recorrido r WHERE r.linea.id = ?1 AND r.activo is TRUE")
    List<Recorrido> findActivos( long idlinea );

    @Query("SELECT r FROM Recorrido r WHERE r.linea.id = ?1 AND r.activo is FALSE")
    List<Recorrido> findNoActivos( long idlinea );

    @Query("SELECT r FROM Recorrido r WHERE r.linea.id = ?1")
    List<Recorrido> findAll( long idlinea );



    //para App Colectivo. se cambio, decia r.activo is ACTIVO (?)
    @Query("SELECT r FROM Recorrido r WHERE r.linea.denominacion = ?1 AND r.activo is TRUE")
    List<Recorrido> findActivosByDenomLinea( String denomLinea);
    
    //OJO con este metodo, si hay dos denom iguales va a traer las dos, tiene que estar asociado si o si con la linea.
    //para App Colectivo
    @Query("SELECT r FROM Recorrido r WHERE r.denominacion = ?1")
    Recorrido findRecorridoByDenom( String denomRecorrido);

    //para App Colectivo. obsoleto
    @Query("SELECT r FROM Recorrido r WHERE r.linea.denominacion = ?1 AND r.denominacion = ?2")
    Recorrido findRecorridoByLineaDenomYRecorridoDenom( String lineaDenom, String recorridoDenom);

    //para App Colectivo. nuevo. trae waypoints recorrido activo
    @Query("SELECT r FROM Recorrido r WHERE r.linea.denominacion = ?1 AND r.denominacion = ?2 AND r.activo is TRUE")
    Recorrido findRecorridoActivoByLineaDenomYRecorridoDenom( String lineaDenom, String recorridoDenom);
   

    //para App Colectivo
    //@Query(value = "SELECT ST_DWithin(('POINT(?1"+" "+"?2)'),(SELECT ST_AsText(ST_MakeLine(trayectos)) AS ge FROM recorridos WHERE recorrido_id = ?3 ),0.001);", nativeQuery = true)
    
    //@Query(value = "SELECT ST_DWithin(('POINT(:lat"+" "+":lng)'),(SELECT ST_AsText(ST_MakeLine(trayectos)) AS ge FROM recorridos WHERE id = :idRec),0.001); ", nativeQuery = true)
    //@Query(value = "SELECT ST_DWithin(('POINT(:latitud,:longitud)'),(SELECT ST_AsText(ST_MakeLine(trayectos)) AS ge FROM recorridos WHERE id = :idRec),0.001) ", nativeQuery=true)

    @Query(value = "SELECT ST_DWithin(('POINT(' || :latitud || ' ' || :longitud || ')'),(SELECT ST_AsText(ST_MakeLine(trayectos)) AS ge FROM recorridos WHERE id = :idRec),0.001) ", nativeQuery=true)
    Boolean verificarUnidadEnRecorrido(@Param("latitud") String latitud,@Param("longitud") String longitud, @Param("idRec") long idRec);
    
    //Query q = getEntityManager().createNativeQuery("SELECT ST_DWithin(('POINT("+ lat +" "+ lng +")'),(SELECT ST_AsText(ST_MakeLine(trayectos)) AS ge FROM recorrido WHERE recorrido_id = "+ idrec +"),0.001); ");    

    //para App Pasajero
    @Query("SELECT r FROM Recorrido r WHERE r.linea.id = ?1 AND r.activo is ACTIVO")
    List<Recorrido> findActivosByIdLinea( int idLinea);


    // prueba punto intermedio
    //@Query(value = "SELECT ST_AsEWKT(ST_LineInterpolatePoint( ?1 ,0.5))", nativeQuery=true)
    @Query(value = "SELECT ST_AsEWKT(ST_LineInterpolatePoint( ?1 ,0.5))", nativeQuery=true)
    String crearPuntoIntermedioTrayecto(LineString lineStringIntermedio);
    
}
