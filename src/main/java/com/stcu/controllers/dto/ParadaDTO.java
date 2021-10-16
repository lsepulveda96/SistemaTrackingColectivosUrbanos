package com.stcu.controllers.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.stcu.model.Parada;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class ParadaDTO implements Serializable {
    
    private long codigo;

    private String direccion;

    private String descripcion;
    
    private String estado;

    private double lat;

    private double lng;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Parada ToParada() {
        Parada parada = new Parada();
        parada.setCodigo(codigo);
        parada.setDireccion(direccion);
        parada.setDescripcion(descripcion);
        parada.setEstado(estado);
        
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate( lat, lng ));
        parada.setCoordenadas( point );
        
        return parada;
    }

    public static List<ParadaDTO> toListDto( List<Parada> paradas ) {
        List<ParadaDTO> paradasDto = new ArrayList<ParadaDTO>();
        paradas.forEach( par ->  {
            ParadaDTO pd = new ParadaDTO();
            pd.setCodigo( par.getCodigo() );
            pd.setDireccion( par.getDireccion() );
            pd.setDescripcion(par.getDescripcion());
            pd.setEstado( par.getEstado());
            Point point = par.getCoordenadas();
            pd.setLat( point.getX() );
            pd.setLng( point.getY() );

            paradasDto.add( pd );
        });
        return paradasDto;
    }

    public static ParadaDTO toDto( Parada parada ) {
        ParadaDTO paradaDto = new ParadaDTO();
        paradaDto.setCodigo( parada.getCodigo() );
        paradaDto.setDireccion( parada.getDireccion() );
        paradaDto.setDescripcion( parada.getDescripcion() );
        paradaDto.setEstado( parada.getEstado() );
        Point point = parada.getCoordenadas();
        paradaDto.setLat( point.getX() );
        paradaDto.setLng( point.getY() );
        return paradaDto;
    }
    
}
