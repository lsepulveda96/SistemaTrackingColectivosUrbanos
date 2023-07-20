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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="COLECTIVOS")
@Getter @Setter @ToString
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


}
