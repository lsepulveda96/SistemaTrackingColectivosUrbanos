package com.stcu.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id; 

    @Column(name="usuario", nullable = false, unique = true)
    private String usuario;

    @Column(name="password", nullable = false)
    private String passwd;

    @Column( name="nombre")
    private String nombre;

    @Column( name="apellido")
    private String apellido;

    @Column(name="dni")
    private String dni;

    @Column(name="direccion")
    private String direccion;

    @Column(name="telefono")
    private String telefono;

    @Column(name="email")
    private String email;

    @Column(name="estado", nullable = false)
    private String estado;

    @Column(name="super", columnDefinition = "boolean default false")
    private boolean superusuario;

    @Column(name="alta")
    @Temporal( TemporalType.DATE )
    private Calendar alta;

    @Column( name="baja")
    @Temporal( TemporalType.DATE )
    private Calendar baja;


    public Usuario( String usr, String passwd ) {
        this.usuario = usr;
        this.passwd = passwd;
    }

    public Usuario() {
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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isSuperusuario() {
        return superusuario;
    }

    public void setSuperusuario(boolean superusuario) {
        this.superusuario = superusuario;
    }

    public Calendar getAlta() {
        return alta;
    }

    public void setAlta(Calendar alta) {
        this.alta = alta;
    }

    public Calendar getBaja() {
        return baja;
    }

    public void setBaja(Calendar baja) {
        this.baja = baja;
    }

    @Override
    public String toString() {
        return "Usuario [alta=" + alta + ", apellido=" + apellido + ", baja=" + baja + ", direccion=" + direccion
                + ", dni=" + dni + ", email=" + email + ", estado=" + estado + ", id=" + id + ", nombre=" + nombre
                + ", passwd=" + passwd + ", superusuario=" + superusuario + ", telefono=" + telefono + ", usuario="
                + usuario + "]";
    }

    

}
