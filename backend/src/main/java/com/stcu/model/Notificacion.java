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

import org.locationtech.jts.geom.Point;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "NOTIFICACIONES")
@Getter
@Setter
@ToString
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificacion_id")
    private long id;

    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fecha;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "activa")
    private Boolean activa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_rec_id")
    private ColectivoRecorrido colectivoRecorrido;

    // agregado para app colectivo
    @Column(name = "COORDENADAS", columnDefinition = "Geometry")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point coordenadas;

    public Notificacion() {
    }

}
