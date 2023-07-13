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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.locationtech.jts.geom.Point;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "UBICACIONES")
@Getter
@Setter
@ToString
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ubicacion_id")
    private long id;

    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fecha;

    @Column(name = "coordenada", columnDefinition = "Geometry")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point coordenada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_rec_id")
    private ColectivoRecorrido colectivoRecorrido;

    public Ubicacion() {
    }
}
