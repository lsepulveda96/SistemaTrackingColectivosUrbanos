package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.stcu.model.Linea;

public interface LineaRepository extends JpaRepository<Linea, Long> {

    List<Linea> findAll();

    Linea findById( long id );
}
