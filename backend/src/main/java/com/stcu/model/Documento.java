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
@Table(name = "documentos")
@Getter @Setter @ToString
public class Documento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="nombre")
    private String nombre; // nombre de la documentacion.

    @Column(name="pathfile",nullable = false)
    private String pathfile; // nombre en el sistema de archivos documento pdf o imagen.

    @Column(name="namefile")
    private String namefile;

    @Column(name="vence")
    private boolean vence; // si tiene vencimiento o no.

    @Column(name="vencimiento")
    @Temporal(TemporalType.DATE)
    private Calendar vencimiento; // fecha de vencimiento si la tiene.
    
    public Documento() {}

    public Documento( long id, String nombre, String namefile,String path, boolean vence, Calendar vencimiento ) {
        this.id = id;
        this.nombre = nombre;
        this.pathfile = path;
        this.vence = vence;
        this.vencimiento = vencimiento;
    }

    public Documento( String nombre, String namefile, String path, boolean vence, Calendar vencimiento ) {
        this.nombre = nombre;
        this.pathfile = path;
        this.namefile = namefile;
        this.vence = vence;
        this.vencimiento = vencimiento;
    }
}
