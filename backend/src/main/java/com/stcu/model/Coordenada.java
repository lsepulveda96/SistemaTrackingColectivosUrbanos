package com.stcu.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "COORDENADAS")
@Getter
@Setter
@ToString
public class Coordenada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private double lat;

    private double lng;

    public Coordenada() {
    }  

    public Coordenada(double x, double y) {
        this.lat = x;
        this.lng = y;
    }

    /*
    public Coordenada(long id, double x, double y) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }
    */

}
