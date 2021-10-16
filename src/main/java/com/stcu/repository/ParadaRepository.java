package com.stcu.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.stcu.model.Parada;

@Repository
public interface ParadaRepository extends JpaRepository<Parada,Long> {
    
    List<Parada> findAll();

    Parada findByCodigo( long codigo );
}
