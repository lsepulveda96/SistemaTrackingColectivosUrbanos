package com.stcu.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.locationtech.jts.geom.LineString;

@Entity
@Table(name="RECORRIDOS")
public class Recorrido {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name ="fecha_inicio")
    @Temporal(TemporalType.DATE)    
    private Calendar fechaInicio;

    @Column(name="fecha_fin")
    @Temporal(TemporalType.DATE)
    private Calendar fechaFin;

    @Column(name="activo")
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "linea_id",nullable=false)
    private Linea linea;

    /* @OneToMany(mappedBy="recorrido", fetch=FetchType.LAZY)
    private List<ParadaRecorrido> paradas; */

    @Column(name="trayectos", columnDefinition="Geometry")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private LineString trayectos;

    @Column(name="waypoints", columnDefinition="Geometry")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private LineString waypoints;

    public Recorrido() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Calendar getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Calendar fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Linea getLinea() {
        return linea;
    }

    public void setLinea(Linea linea) {
        this.linea = linea;
    }

    public LineString getTrayectos() {
        return this.trayectos;
    }

    public void setTrayectos( LineString trayectos ) {
        this.trayectos = trayectos;
    }

    public LineString getWaypoints() {
        return this.waypoints;
    }

    public void setWaypoints( LineString wp ) {
        this.waypoints = wp;
    }

    public String toString() {
        return "Recorrido [id =" + id + ", fecha_inicio =" + fechaInicio + ", fecha_fin =" + fechaFin + ", activo =" + activo + 
                ", Linea =" + linea.getId() + ", " + 
                ", trayectos =" + trayectos + 
                ", waypoints =" + waypoints + "]";
    }
}
