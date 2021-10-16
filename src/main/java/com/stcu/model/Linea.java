package com.stcu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LINEAS")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String desc ) {
        this.descripcion = desc;
    }

    public boolean isEnServicio() {
        return enServicio;
    }

    public void setEnServicio(boolean enServicio) {
        this.enServicio = enServicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
