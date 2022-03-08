package com.stcu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "UBICACIONES")
@Getter @Setter @ToString
public class Ubicacion {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ubicacion_id")
    private long id;

    @Column(name="fecha")
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="linea_colectivo_id")
    private LineaColectivo lineaColectivo;

    public Ubicacion() {}
}
