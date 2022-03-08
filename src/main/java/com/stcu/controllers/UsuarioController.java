package com.stcu.controllers;

import java.util.Calendar;
import java.util.List;

import com.stcu.controllers.dto.UsuarioDTO;
import com.stcu.model.Usuario;
import com.stcu.services.UsuarioServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    
    @Autowired
    UsuarioServiceImp service;

    @GetMapping("/usuarios")
    public String getUsuarios() {
        List<Usuario> usrs = service.getAllUsuarios();
        
        Response<List<UsuarioDTO>> response = new Response<List<UsuarioDTO>>( false, 200, "Lista de usuarios", UsuarioDTO.toListUsuarioDTO(usrs) );

        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/usuario/{id}")
    public String getUsuario( @PathVariable long id ) {
        Usuario usr = service.getUsuario(id);

        Response<UsuarioDTO> response;

        if (usr != null)
            response = new Response<UsuarioDTO>( false, 200, "Usuario " + id, new UsuarioDTO(usr) );
        else 
            response = new Response<UsuarioDTO>( true, 400, "No se pudo encontrar usuario " + id, null );
    
        return Mapper.getResponseAsJson(response);    
    }

    @PostMapping("/usuarios")
    public String saveUsuario( @RequestBody Usuario usr ) {
        Response<UsuarioDTO> response;

        Usuario usuario = service.getUsuario(usr.getUsuario() );
        
        if (usuario != null) {
            response = new Response<UsuarioDTO>( true, 400, "El nombre de usuario ya se encuentra registrado", null );
            return Mapper.getResponseAsJson(response);
        } 
        else {
            usr.setAlta( Calendar.getInstance() );
            usuario = service.saveUsuario( usr );
            if (usuario != null)
                response = new Response<UsuarioDTO>( false, 200, "Nuevo usuario registrado", new UsuarioDTO(usr) );
            else 
                response = new Response<UsuarioDTO>( true, 400, "No se pudo registrar nuevo usuario", null );
    
            return Mapper.getResponseAsJson(response);
        }
    }

    @PutMapping("/usuario/{id}")
    public String updateUsuario( @PathVariable long id, @RequestBody Usuario usr ) {

        Usuario usuario = service.updateUsuario(id, usr );

        Response<UsuarioDTO> response;

        if (usuario != null)
            response = new Response<UsuarioDTO>( false, 200, "Usuario " + id + " actualizado", new UsuarioDTO(usuario) );
        else 
            response = new Response<UsuarioDTO>( true, 400, "No se pudo actualizar usuario " + id, null );
        
        return Mapper.getResponseAsJson(response);
    }
}
