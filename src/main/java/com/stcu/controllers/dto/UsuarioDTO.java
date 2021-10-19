package com.stcu.controllers.dto;

import java.util.ArrayList;
import java.util.List;

import com.stcu.model.Usuario;

public class UsuarioDTO {
    
    private long id;

    private String usuario;
    
    private String nombre;

    private String apellido;

    private String dni;

    private String direccion;

    private String telefono;

    private String email;

    private boolean superusuario;

    private String estado;

    public UsuarioDTO() {}  
    
    public UsuarioDTO( Usuario usr ) {
        id = usr.getId();
        usuario = usr.getUsuario();
        nombre = usr.getNombre();
        apellido = usr.getApellido();
        dni = usr.getDni();
        direccion = usr.getDireccion();
        telefono = usr.getTelefono();
        email = usr.getEmail();
        superusuario = usr.isSuperusuario();
        estado = usr.getEstado();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getSuperusuario() {
        return superusuario;
    }

    public void setSuperusuario( Boolean superusuario) {
        this.superusuario = superusuario;
    }

    public String isEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public static List<UsuarioDTO> toListUsuarioDTO( List<Usuario> listUsuarios ) {
        List<UsuarioDTO> list = new ArrayList<UsuarioDTO>();
        listUsuarios.forEach( usr -> {
            list.add( toDTO( usr ) );
        });
        return list;
    }

    private static UsuarioDTO toDTO( Usuario usr ) {
        UsuarioDTO usrDto = new UsuarioDTO();
        usrDto.setId( usr.getId() );
        usrDto.setUsuario( usr.getUsuario() );
        usrDto.setNombre(usr.getNombre() );
        usrDto.setApellido( usr.getApellido() );
        usrDto.setDni( usr.getDni() );
        usrDto.setDireccion( usr.getDireccion() );
        usrDto.setTelefono(usr.getTelefono() );
        usrDto.setEmail(usr.getEmail());
        usrDto.setSuperusuario( usr.isSuperusuario() );
        usrDto.setEstado(usr.getEstado());

        return usrDto;
    }

    @Override
    public String toString() {
        return "UsuarioDTO [apellido=" + apellido + ", direccion=" + direccion + ", dni=" + dni + ", email=" + email
                + ", estado=" + estado + ", id=" + id + ", nombre=" + nombre + ", superusuario=" + superusuario
                + ", telefono=" + telefono + ", usuario=" + usuario + "]";
    }

    
    
}
