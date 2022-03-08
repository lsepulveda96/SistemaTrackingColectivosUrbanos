package com.stcu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="LINEAS")
@Getter @Setter @ToString
public class Linea {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="denominacion", nullable=false, unique=true)
    private String denominacion;

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="esServicio")
    private boolean enServicio;
    
    @Column(name="estado")
    private String estado;

    public Linea() { }
    
    public Linea( long id, String denominacion, String descripcion, boolean enServicio, String estado ) {
        this.id = id;
        this.denominacion = denominacion;
        this.descripcion = descripcion;
        this.enServicio = enServicio;
        this.estado = estado;
    }

}
