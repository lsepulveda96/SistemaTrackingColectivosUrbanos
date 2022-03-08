package com.stcu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="NOTIFICACIONES")
@Getter @Setter @ToString
public class Notificacion {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notificacion_id")
    private long id;

    @Column(name="fecha")
    private Date fecha;

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="tipo")
    private String tipo;

    @Column(name="activa")
    private boolean activa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="linea_colectivo_id")
    private LineaColectivo lineaColectivo;

    private Notificacion() {}

}
