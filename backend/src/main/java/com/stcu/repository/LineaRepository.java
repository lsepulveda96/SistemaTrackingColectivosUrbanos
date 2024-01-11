package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.stcu.model.Linea;

public interface LineaRepository extends JpaRepository<Linea, Long> {

    List<Linea> findAll();

    Linea findById(long id);

    // para AppPasajero
    @Query("SELECT l FROM Linea l WHERE l.estado LIKE 'ACTIVA'")
    List<Linea> findLineasActivas();
}
