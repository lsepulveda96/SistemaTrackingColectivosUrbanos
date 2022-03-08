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
@Table(name="LINEA_COLECTIVO")
@Getter @Setter @ToString
public class LineaColectivo {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="linea_colectivo_id")
    private long id;

    @Column(name="fecha_desde")
    private Date fechaDesde;

    @Column(name="fecha_hasta")
    private Date fechaHasta;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="linea_id")
    private Linea linea;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="colectivo_id")
    private Colectivo colectivo;

    
    private LineaColectivo() {
    }
}
