package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.stcu.model.Parada;

public interface ParadaRepository extends JpaRepository<Parada,Long> {
    
    List<Parada> findAll();

    Parada findByCodigo( long codigo );

    @Query("SELECT p FROM Parada p WHERE p.estado LIKE 'HABILITADA'")
    List<Parada> findAllActivas();
}
