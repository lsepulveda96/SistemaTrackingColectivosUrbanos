package com.stcu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stcu.model.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    @Query("SELECT u FROM Ubicacion u WHERE u.colectivoRecorrido.id =:idCR")
    public List<Ubicacion> findByColectivoRecorrido(@Param("idCR") long crId);

    @Query("SELECT u FROM Ubicacion u WHERE u.colectivoRecorrido.id =:idCR AND " +
          "u.fecha = (SELECT max(fecha) FROM Ubicacion ub WHERE ub.colectivoRecorrido =:idCR)")
    public Ubicacion findLastByColectivoRecorrido(@Param("idCR") long crId);
    
}
