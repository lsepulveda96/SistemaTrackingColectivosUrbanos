package com.stcu.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stcu.model.Documento;

public interface DocumentoRepository extends JpaRepository<Documento,Long> {

    Documento findById(long id);

    List<Documento> findAllByVenceTrueAndVencimientoBefore( Calendar fecha );
 }
