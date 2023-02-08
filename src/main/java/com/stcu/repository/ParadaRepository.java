package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.stcu.model.Parada;

public interface ParadaRepository extends JpaRepository<Parada,Long> {
    
    List<Parada> findAll();

    Parada findByCodigo( long codigo );
}
