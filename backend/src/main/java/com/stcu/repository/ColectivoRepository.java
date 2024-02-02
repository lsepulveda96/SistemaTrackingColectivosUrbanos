package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;

import com.stcu.model.Colectivo;

public interface ColectivoRepository extends JpaRepository<Colectivo,Long> {

    List<Colectivo> findAll();

    Colectivo findById( long id );

     //para AppColectivo
    @Query("SELECT c FROM Colectivo c WHERE c.unidad = ?1")
    Colectivo findByUnidad(String unidad);

     //para AppColectivo
    @Query("SELECT c FROM Colectivo c WHERE c.enCirculacion IS FALSE")
    List<Colectivo> findAllColectivosSinCircular();

    //@Query("SELECT c FROM Colectivo c WHERE EXISTS (SELECT d FROM c.documentos d WHERE d.vence = TRUE AND d.vencimiento < ?1)")
    @Query("SELECT DISTINCT c FROM Colectivo c JOIN FETCH c.documentos d WHERE d.vence=TRUE AND d.vencimiento < :fecha")
    List<Colectivo> findAllColectivosDocsVencidos( Calendar fecha );
}
