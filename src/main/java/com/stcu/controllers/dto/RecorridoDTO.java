package com.stcu.controllers.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.stcu.model.Linea;
import com.stcu.model.Recorrido;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class RecorridoDTO implements Serializable {

    class PointLS {
        double lat;
        double lng;
        PointLS( double la, double ln ) {
            lat = la;
            lng = ln;
        }
    }

    private long id;
    private Calendar fechaInicio;
    private Calendar fechaFin;
    private boolean activo;

    private Linea linea;

    private List<PointLS> trayectos;
    private List<PointLS> waypoints;
    
    public RecorridoDTO() { }

    public RecorridoDTO( Recorrido rec ) {
        this.id = rec.getId();
        this.fechaInicio = rec.getFechaInicio();
        this.fechaFin = rec.getFechaFin();
        this.activo = rec.isActivo();
        this.linea = rec.getLinea();

        LineString trays = rec.getTrayectos();
        trayectos = new ArrayList<PointLS>();
        for (int i = 0; i < trays.getNumPoints(); i++) {
            trayectos.add( new PointLS( trays.getPointN(i).getX(), trays.getPointN(i).getY() ) );
        }
            trays.getPointN( 5 );
        
        LineString wpts = rec.getWaypoints();
        waypoints = new ArrayList<PointLS>();
        for (int i=0; i < wpts.getNumPoints(); i++) {
            waypoints.add( new PointLS( wpts.getPointN(i).getX(), wpts.getPointN(i).getY() ) );
        }
    }

    public Recorrido toRecorrido( RecorridoDTO drec ) {

        Recorrido rec = new Recorrido();
        rec.setId( drec.getId() );
        rec.setFechaInicio( drec.getFechaInicio());
        rec.setFechaFin( drec.getFechaFin() );
        rec.setActivo( drec.isActivo() );
        rec.setLinea( drec.getLinea() );
        
        //GeometryFactory geometryFactory = new GeometryFactory();

        return rec;
        
    }

    private static RecorridoDTO toDTO( Recorrido recorrido ) {
        RecorridoDTO rd = new RecorridoDTO();
        rd.setId( recorrido.getId() );
        rd.setFechaInicio( recorrido.getFechaInicio() );
        rd.setFechaFin( recorrido.getFechaFin() );
        rd.setActivo( recorrido.isActivo() );
        
        return rd;
    } 

    public static List<RecorridoDTO>toListRecorridosDTO( List<Recorrido> recorridos ) {
        List<RecorridoDTO> list = new ArrayList<RecorridoDTO>();
        recorridos.forEach( rec -> {
            list.add( toDTO( rec ));
        });
        return list;
    }

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

    public List<PointLS> getTrayectos() {
        return trayectos;
    }

    public void setTrayectos(List<PointLS> trayectos) {
        this.trayectos = trayectos;
    }

    public List<PointLS> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<PointLS> waypoints) {
        this.waypoints = waypoints;
    }
    
    
}
