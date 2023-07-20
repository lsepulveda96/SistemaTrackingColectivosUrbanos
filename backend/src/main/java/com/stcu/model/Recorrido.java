package com.stcu.model;

import java.util.List;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.locationtech.jts.geom.LineString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="RECORRIDOS")
@Getter @Setter @ToString
public class Recorrido {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="denominacion", nullable = false)
    private String denominacion;

    @Column(name ="fecha_inicio")
    @Temporal(TemporalType.DATE)    
    private Calendar fechaInicio;

    @Column(name="fecha_fin")
    @Temporal(TemporalType.DATE)
    private Calendar fechaFin;

    @Column(name="activo")
    private boolean activo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "linea_id",nullable=false)
    private Linea linea;

    @OneToMany(mappedBy="recorrido", fetch=FetchType.LAZY)
    private List<ParadaRecorrido> paradas; 

    @Column(name="trayectos", columnDefinition="Geometry")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private LineString trayectos;

    @Column(name="waypoints", columnDefinition="Geometry")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private LineString waypoints;

    public Recorrido() {}
    
}
