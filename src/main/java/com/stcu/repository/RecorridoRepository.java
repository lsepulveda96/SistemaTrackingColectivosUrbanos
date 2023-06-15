package com.stcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.stcu.model.Recorrido;

public interface RecorridoRepository extends JpaRepository<Recorrido,Long> {

    Recorrido findById( long id );

    @Query("SELECT r FROM Recorrido r WHERE r.linea.id = ?1 AND r.activo is TRUE")
    List<Recorrido> findActivos( long idlinea );

    @Query("SELECT r FROM Recorrido r WHERE r.linea.id = ?1")
    List<Recorrido> findAll( long idlinea );
}
