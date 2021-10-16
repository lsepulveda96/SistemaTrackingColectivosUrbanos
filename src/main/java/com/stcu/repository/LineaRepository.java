package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.stcu.model.Linea;

@Repository
public interface LineaRepository extends JpaRepository<Linea, Long> {
    List<Linea> findAll();
    Linea findById( long id );
}
