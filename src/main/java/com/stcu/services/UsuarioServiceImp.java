package com.stcu.services;

import java.util.List;

import com.stcu.model.Usuario;
import com.stcu.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private UsuarioRepository rep;

    @Override
    public List<Usuario> getAllUsuarios() {
        return this.rep.findAll();
    }

    @Override
    public Usuario getUsuario(long id) {
        return this.rep.findById(id);
    }

    public Usuario getUsuario( String usr ) {
        return this.rep.findByUsuario( usr );
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Encripta password
        usuario.setPasswd( encoder.encode( usuario.getPasswd() ) );

        return this.rep.save(usuario);
    }

    @Override
    public Usuario updateUsuario(long id, Usuario usuario) {
        Usuario usr = this.rep.findById(id);
        usr.setUsuario(usuario.getUsuario());
        usr.setNombre(usuario.getNombre());
        usr.setApellido(usuario.getApellido());
        usr.setDni(usuario.getDni());
        usr.setDireccion(usuario.getDireccion());
        usr.setEmail(usuario.getEmail());
        usr.setSuperusuario(usuario.isSuperusuario());
        usr.setTelefono(usuario.getTelefono());

        return this.rep.save(usr);
    }

    
    @Override
    public boolean validateUsuario( String username, String pwd ) {

        Usuario usuario = this.rep.findByUsuario(username);
        if (usuario != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches( pwd, usuario.getPasswd() );
        }
        return false;
    }

    @Override
    public boolean changePass(long id, String pass, String newpass) {
        Usuario usuario = this.rep.findById(id);
        if (usuario != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches( pass, usuario.getPasswd() ) )
                return false;
            usuario.setPasswd( encoder.encode( newpass ));
            this.rep.save( usuario );
        }
        return false;
    }

    
    
}
