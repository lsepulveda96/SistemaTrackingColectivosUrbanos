package com.stcu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//import org.springframework.data.geo.Point;
import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity 
@Table(name="PARADAS")
@Getter @Setter @ToString
public class Parada {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name="CODIGO")
    private long codigo;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name="DESCRIPCION")
    private String descripcion;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "COORDENADAS", columnDefinition = "Geometry")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point coordenadas;

    public Parada() {}

    public Parada( long cod, String dir, Point co ) {
        this.codigo = cod;
        this.direccion = dir;
        this.coordenadas = co;
    }
    

}
