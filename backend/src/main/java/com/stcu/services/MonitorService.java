package com.stcu.services;

import java.util.Calendar;
import java.util.List;

import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Notificacion;
import com.stcu.model.Ubicacion;

public interface MonitorService {
    
    public List<ColectivoRecorrido> getColectivosTransito();

    public ColectivoRecorrido getColectivoRecorrido( long idcr );

    public ColectivoRecorrido saveColectivoRecorrido( ColectivoRecorrido cr );

    public Ubicacion saveUbicacion( Ubicacion ubicacion );

    public Ubicacion getLastUbicacion( long cr );

    public List<Ubicacion> findUbicaciones( long cr );

     // para app colectivo
    public ColectivoRecorrido updateParadaActual(String denomLinea, String denomRecorrido, String unidad, Long codigo);

    // para app colectivo
    public ColectivoRecorrido getColectivoRecorridoByLinDenomRecDenomColUnidad(String denomLinea, String denomRecorrido,
            String unidad);

    // para app colectivo
    public Notificacion notificacionDesvio(ColectivoRecorrido cr, String latitud, String longitud,
            boolean notificacionEstaActiva);

    // para app colectivo
    public Notificacion createNotificacion(ColectivoRecorrido cr, String latitud, String longitud, String descripcion,
            Boolean notificacionEstaActiva);

    // para app colectivo
    public Boolean desvioActivo(long idcr);

    // para app colectivo
    public Notificacion finNotificacionDesvio(long idcr, boolean notificacionEstaActiva);

    // para app colectivo
    public ColectivoRecorrido bajaColectivoRecorrido(String denomLinea, String unidad, String denomRecorrido,
            Calendar calendar, boolean enTransito);

    // para app colectivo
    public Ubicacion createUbicacion(ColectivoRecorrido cr, String latitud, String longitud);

    // para app colectivo
    public Notificacion updateNotificacion(ColectivoRecorrido cr, String latitud, String longitud,
            String segundosDetenidoStr, Boolean notificacionEstaActiva);

    // para app colectivo
    public Notificacion finNotificacionColeDetenido(ColectivoRecorrido cr, String latitud, String longitud,
            String segundosDetenidoStr, Boolean notificacionEstaActiva);

    // para detener colectivo desde pagina monitoreo app escritorio
    public ColectivoRecorrido detenerColectivoRecorrido(long idColRec, long idLinea, boolean disable);

    public ColectivoRecorrido detenerColectivoRecorridoByDenom(String denomLinea, String unidad, String denomRecorrido,
            boolean disabled);

    // para pantalla notificaciones activas
    public List<Notificacion> findNotificacionesActivas();


    // para notificacion fin desvio, trae cr fuera de circulacion
    public ColectivoRecorrido getCrByLinRecColDenomFueraDeCirculacion(String denomLinea,String denomRecorrido,String unidad);

}
