package com.stcu.controllers.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.stcu.model.Linea;
import com.stcu.model.Recorrido;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class RecorridoDTO implements Serializable {

    private long id;
    private Calendar fechaInicio;
    private Calendar fechaFin;
    private boolean activo;

    private Linea linea;

    private List<CoordenadaDTO> trayectos;
    private List<CoordenadaDTO> waypoints;
    
    public RecorridoDTO() { }

    public RecorridoDTO( Recorrido rec ) {
        this.id = rec.getId();
        this.fechaInicio = rec.getFechaInicio();
        this.fechaFin = rec.getFechaFin();
        this.activo = rec.isActivo();
        this.linea = rec.getLinea();

        LineString trays = rec.getTrayectos();
        trayectos = new ArrayList<CoordenadaDTO>();
        for (int i = 0; i < trays.getNumPoints(); i++) {
            trayectos.add( new CoordenadaDTO( trays.getPointN(i).getX(), trays.getPointN(i).getY() ) );
        }
        
        LineString wpts = rec.getWaypoints();
        waypoints = new ArrayList<CoordenadaDTO>();
        for (int i=0; i < wpts.getNumPoints(); i++) {
            waypoints.add( new CoordenadaDTO( wpts.getPointN(i).getX(), wpts.getPointN(i).getY() ) );
        }
    }

    public Recorrido toRecorrido( RecorridoDTO drec ) {

        Recorrido rec = new Recorrido();
        rec.setId( drec.getId() );
        rec.setFechaInicio( drec.getFechaInicio());
        rec.setFechaFin( drec.getFechaFin() );
        rec.setActivo( drec.isActivo() );
        rec.setLinea( drec.getLinea() );
        
        GeometryFactory geometryFactory = new GeometryFactory();

        Coordinate[] tpoints = new Coordinate[ drec.trayectos.size() ];
        for (int i=0; i < drec.trayectos.size() ; i++) {
            CoordenadaDTO coo = drec.trayectos.get(i);
            tpoints[i] = new Coordinate( coo.getLat(), coo.getLng() );
        }
        rec.setTrayectos( geometryFactory.createLineString( tpoints ));

        Coordinate[] wpoints = new Coordinate[ drec.waypoints.size() ];
        for (int i=0; i < drec.waypoints.size() ; i++) {
            CoordenadaDTO coo = drec.waypoints.get(i);
            wpoints[i] = new Coordinate( coo.getLat(), coo.getLng() );
        }
        rec.setWaypoints( geometryFactory.createLineString( wpoints ));

    
        return rec;    
    }

    private static RecorridoDTO toDTO( Recorrido recorrido ) {
        RecorridoDTO rd = new RecorridoDTO();
        rd.setId( recorrido.getId() );
        rd.setFechaInicio( recorrido.getFechaInicio() );
        rd.setFechaFin( recorrido.getFechaFin() );
        rd.setActivo( recorrido.isActivo() );
        
        LineString trays = recorrido.getTrayectos();
        List<CoordenadaDTO> trec = new ArrayList<CoordenadaDTO>();
        for (int i = 0; i < trays.getNumPoints(); i++) {
            trec.add( new CoordenadaDTO( trays.getPointN(i).getX(), trays.getPointN(i).getY() ) );
        }
        // trays.getPointN( 5 );
        rd.setTrayectos( trec );

        LineString wpts = recorrido.getWaypoints();
        List<CoordenadaDTO> wrec = new ArrayList<CoordenadaDTO>();
        for (int i=0; i < wpts.getNumPoints(); i++) {
            wrec.add( new CoordenadaDTO( wpts.getPointN(i).getX(), wpts.getPointN(i).getY() ) );
        }
        rd.setWaypoints( wrec );

        return rd;
    } 

    public static List<RecorridoDTO>toListRecorridosDTO( List<Recorrido> recorridos ) {
        List<RecorridoDTO> list = new ArrayList<RecorridoDTO>();
        recorridos.forEach( rec -> {
            list.add( toDTO( rec ));
        });
        return list;
    }
    
}
