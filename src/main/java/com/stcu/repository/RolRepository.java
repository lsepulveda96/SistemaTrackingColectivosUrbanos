package com.stcu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.stcu.model.ERole;
import com.stcu.model.Rol;

//@Repository
public interface RolRepository  extends JpaRepository<Rol, Long> {
    
    Optional<Rol> findByRol(ERole rol);
}
