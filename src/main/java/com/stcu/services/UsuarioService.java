package com.stcu.services;

import java.util.List;

import com.stcu.model.Usuario;

public interface UsuarioService {

    public List<Usuario> getAllUsuarios();

    public Usuario getUsuario( long id );

    public Usuario saveUsuario( Usuario usuario );

    public Usuario updateUsuario( long id, Usuario usuario );

    public boolean validateUsuario( String username, String pwd );

    public boolean changePass( long id, String pass, String newpass );
    
}
