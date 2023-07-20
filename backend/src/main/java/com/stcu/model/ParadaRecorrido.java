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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="PARADA_RECORRIDO")
@Getter @Setter @ToString
public class ParadaRecorrido {
 
    @Id @GeneratedValue( strategy=GenerationType.IDENTITY)
    @Column(name="parada_recorrido_id")
    private long id;

    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="parada_id")
    private Parada parada;

    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="recorrido_id")
    private Recorrido recorrido;

    // Orden secuencial de la parada en el recorrido.
    @Column(name="orden")
    private int orden;

    // Distancia a la siguiente parada en mts (orden siguiente)
    @Column(name="distancia")
    private int distancia;

    // Tiempo a la siguiente parada (orden siguiente), en minutos.
    @Column(name="tiempo")
    private double tiempo;

    public ParadaRecorrido() {}

    public ParadaRecorrido( Parada p, Recorrido r ) {
        this.parada = p;
        this.recorrido = r;
    }
}