package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.stcu.model.Colectivo;

public interface ColectivoRepository extends JpaRepository<Colectivo,Long> {

    List<Colectivo> findAll();

    Colectivo findById( long id );
}
