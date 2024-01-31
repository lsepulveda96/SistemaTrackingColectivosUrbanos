package com.stcu.services;

import java.util.List;

import org.locationtech.jts.geom.LineString;

import com.stcu.model.ParadaRecorrido;
import com.stcu.model.Recorrido;
import com.stcu.model.Ubicacion;

public interface RecorridoService {
    
    public List<Recorrido> getRecorridosLinea( long idlinea );

    public Recorrido getRecorrido( long id );

    public List<Recorrido> getRecorridosActivos( long idlinea );

    public List<Recorrido> getRecorridosNoActivos( long idlinea );
    
    public Recorrido saveRecorrido( Recorrido recorrido );

    public Recorrido updateRecorrido( long id, Recorrido recorrido );

    public List<ParadaRecorrido> getParadasRecorrido( long idrecorrido );

    public long existDenominacion( long idlinea, String denom );

    public Recorrido deactivateRecorrido( long id );


    //Para app colectivo
    public List<Recorrido> getRecorridosActivosByDenomLinea(String denomLinea);

    //Para app colectivo
    public Recorrido getRecorridoByDenom(String denomRecorrido);

    //Para app colectivo
    public List<ParadaRecorrido> getParadasRecorridoByLineaDenomYRecorridoDenom( String lineaDenom, String recorridoDenom );

    //Para app colectivo. obsoleto, no traia activos
    public Recorrido getRecorridoByLineaDenomYRecorridoDenom(String denomLinea, String denomRecorrido);

    // nuevo, para simulacion recorrido, trae activos
    public Recorrido getRecorridoActivoByLineaDenomYRecorridoDenom(String denomLinea, String denomRecorrido);

    // Para app pasajero
    public List<Recorrido> getRecorridosActivosByIdLinea(int idLinea);

    // Para app pasajero
    public List<ParadaRecorrido> getParadasRecorridoByLineaIdYRecorridoId( long idLinea, long idRecorrido );

    // Para traer todas paradas app pasajero
    public List<ParadaRecorrido> getParadasRecorridoByLineaDenom( String lineaDenom );

    // prueba punto intermedio
    public String crearPuntoIntermedio( LineString lineStringIntermedio );

    // para mostrar ubicacion parada pasajero en mapa arribo colectivo mobile
    public ParadaRecorrido getParadaRecorrido(long idLinea, long idRecorrido, long codigoParada);
}
