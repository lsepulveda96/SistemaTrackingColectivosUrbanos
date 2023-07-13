package com.stcu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stcu.model.ColectivoRecorrido;

public interface ColectivoRecorridoRepository extends JpaRepository<ColectivoRecorrido,Long> {
    
    ColectivoRecorrido findById( long id );

    List<ColectivoRecorrido> findByTransitoTrue();
}
