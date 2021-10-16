package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.stcu.model.Colectivo;

@Repository
public interface ColectivoRepository extends JpaRepository<Colectivo,Long> {

    List<Colectivo> findAll();

    Colectivo findById( long id );
}
