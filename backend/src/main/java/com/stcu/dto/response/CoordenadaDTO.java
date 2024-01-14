package com.stcu.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class CoordenadaDTO implements Serializable {

    private double lat;

    private double lng;

    public CoordenadaDTO() {
    }

    public CoordenadaDTO(double x, double y) {
        this.lat = x;
        this.lng = y;
    }

    // Coordenada2
    private String direccion;
    private int codigo;

    // Coordenada3
    private Date fechaNotificacion;

    // Coordenada4
    private int orden;

    public CoordenadaDTO(Object[] columns) {
        switch (columns.length) {
            case 2:
                // Coordenada1
                this.lat = (double) columns[0];
                this.lng = (double) columns[1];
                break;
            case 3:
                // Coordenada3
                this.lat = (double) columns[0];
                this.lng = (double) columns[1];
                this.fechaNotificacion = (Date) columns[2];
                break;
            case 4:
                // Coordenada2 y 4
                this.lat = (double) columns[0];
                this.lng = (double) columns[1];
                this.direccion = (String) columns[2];
                this.codigo = (int) columns[3];
                break;
        }
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double l) {
        this.lat = l;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double l) {
        this.lng = l;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCodigo() {
        return this.codigo;
    }

    public void setCodigo(int cod) {
        this.codigo = cod;
    }

    public Date getFechaNotificacion() {
        return this.fechaNotificacion;
    }

    public void setFechaNotificacion(Date fn) {
        this.fechaNotificacion = fn;
    }

    public int getOrden() {
        return this.orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

}
