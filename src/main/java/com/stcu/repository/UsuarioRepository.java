package com.stcu.repository;

import java.util.List;

import com.stcu.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    List<Usuario> findAll();

    Usuario findById( long id );

    Usuario findByUsuario( String usr );    
}
