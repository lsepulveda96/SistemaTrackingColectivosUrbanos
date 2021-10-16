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
@Table(name="COLECTIVOS")
public class Colectivo {
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="unidad", nullable = false, unique = true)
    private String unidad;

    @Column(name="patente", nullable = false, unique = true)
    private String patente;

    @Column(name="marca")
    private String marca;

    @Column(name="modelo")
    private String modelo;

    @Column(name="anio")
    private int anio;

    @Column(name="capacidad")
    private int capacidad;

    @Column(name="fecha_compra")
    @Temporal(TemporalType.DATE)
    private Calendar fechaCompra;

    @Column(name="estado")
    private String estado;

    @Column(name="fecha_baja")
    @Temporal(TemporalType.DATE)
    private Calendar fechaBaja;

    @Column(name="en_circulacion")
    private boolean enCirculacion;

    public Colectivo() {}
    
    public Colectivo( long id, String unidad, String patente ) {
        this.id =id;
        this.unidad = unidad;
        this.patente = patente;
    }

    public long getId() {
        return id;
    }

    public String getUnidad() {
        return unidad;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Calendar getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Calendar fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Calendar getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Calendar fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public boolean isEnCirculacion() {
        return enCirculacion;
    }

    public void setEnCirculacion(boolean enCirculacion) {
        this.enCirculacion = enCirculacion;
    }


}
