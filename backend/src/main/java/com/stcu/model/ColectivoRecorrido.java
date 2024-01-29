package com.stcu.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="COLECTIVO_RECORRIDO")
@Getter @Setter @ToString
public class ColectivoRecorrido {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="col_rec_id")
    private long id;

    @Column(name="desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar desde;

    @Column(name="hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar hasta;

    @Column(name="transito", columnDefinition = "boolean default true")
    private boolean transito;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="recorrido_id")
    private Recorrido recorrido;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="colectivo_id")
    private Colectivo colectivo;

    // para app colectivo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parada_id")
    private Parada paradaActual;

    @Column(name="fecha_parada_actual")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fechaParadaActual;
   
    public ColectivoRecorrido() {}
    
    public ColectivoRecorrido( Colectivo col, Recorrido rec ) {
        this.colectivo = col;
        this.recorrido = rec;
    }

}
