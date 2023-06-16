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
}
