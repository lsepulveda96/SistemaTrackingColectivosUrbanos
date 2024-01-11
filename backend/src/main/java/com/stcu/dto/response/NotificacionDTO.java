package com.stcu.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.stcu.model.Notificacion;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificacionDTO implements Serializable {
    
    private long id;

    private Calendar fecha;

    private String descripcion;
    
    private String tipo;

    private Boolean activa;

    private CoordenadaDTO coordenada;

    private String colectivoDenom;

    private String recorridoDenom;

    private String lineaDenom;


    public NotificacionDTO() {}

    public NotificacionDTO( Notificacion notif ) {
        id = notif.getId();
        fecha = notif.getFecha();
        descripcion = notif.getDescripcion();
        tipo = notif.getTipo();
        activa = notif.getActiva();
        
        Point point = notif.getCoordenadas();
        if (point != null)
            coordenada = new CoordenadaDTO( point.getX(), point.getY() );

        colectivoDenom = notif.getColectivoRecorrido().getColectivo().getUnidad();
        recorridoDenom = notif.getColectivoRecorrido().getRecorrido().getDenominacion();
        lineaDenom = notif.getColectivoRecorrido().getRecorrido().getLinea().getDenominacion();
    }

    /**
     * Convierte el objeto actual paradaDTO en un objeto Notificacion
     * @return parada
     */
    public Notificacion ToNotificacion() {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(id);
        notificacion.setFecha(fecha);
        notificacion.setDescripcion(descripcion);
        notificacion.setTipo(tipo);
        notificacion.setActiva(activa);
        
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate( coordenada.getLat(), coordenada.getLng() ));
        notificacion.setCoordenadas( point );
        
        return notificacion;
    }

    /**
     * Convierte una lista de objetos Notificacion a una lista de objetos NotificacionDTO
     * @param notificacion
     * @return
     */
    public static List<NotificacionDTO> toListNotificacionDTO( List<Notificacion> notificacion ) {
        List<NotificacionDTO> notificacionDto = new ArrayList<NotificacionDTO>();
        notificacion.forEach( notif ->  {
            notificacionDto.add( toDTO( notif ) );
        });
        return notificacionDto;
    }

    /**
     * Convierte un objeto de tipo Notificacion a un objeto de tipo NotificacionDTO.
     * @param notificacion
     * @return
     */
    public static NotificacionDTO toDTO( Notificacion notificacion ) {
        NotificacionDTO notificacionDto = new NotificacionDTO();
        notificacionDto.setId( notificacion.getId());
        notificacionDto.setFecha( notificacion.getFecha() );
        notificacionDto.setDescripcion( notificacion.getDescripcion() );
        notificacionDto.setTipo( notificacion.getTipo() );
        notificacionDto.setActiva( notificacion.getActiva() );
        
        Point point = notificacion.getCoordenadas();
        notificacionDto.setCoordenada( new CoordenadaDTO( point.getX(), point.getY() ));

        notificacionDto.setColectivoDenom(notificacion.getColectivoRecorrido().getColectivo().getUnidad());
        notificacionDto.setRecorridoDenom(notificacion.getColectivoRecorrido().getRecorrido().getDenominacion());
        notificacionDto.setLineaDenom(notificacion.getColectivoRecorrido().getRecorrido().getLinea().getDenominacion());

        return notificacionDto;
    }
    
}
