package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stcu.model.ParadaRecorrido;

public interface ParadaRecorridoRepository extends JpaRepository<ParadaRecorrido,Long>{
    ParadaRecorrido findById(long id);
}
