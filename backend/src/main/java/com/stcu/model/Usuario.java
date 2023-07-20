package com.stcu.model;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="USUARIOS")
@Getter @Setter @ToString
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="usuario_rol", 
            joinColumns = @JoinColumn(name="usuario_id"), 
            inverseJoinColumns = @JoinColumn(name="rol_id"))
    private Set<Rol> roles = new HashSet<>();
    

    public Usuario( String usr, String passwd ) {
        this.usuario = usr;
        this.passwd = passwd;
        this.estado = "ACTIVO";
    }

    public Usuario() {
    }
}
