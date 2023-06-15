package com.stcu.services;

import java.util.List;

import com.stcu.model.Recorrido;

public interface RecorridoService {
    
    public List<Recorrido> getRecorridosLinea( long idlinea );

    public Recorrido getRecorrido( long id );

    public List<Recorrido> getRecorridosActivos( long idlinea );

    public Recorrido saveRecorrido( Recorrido recorrido );

    public Recorrido updateRecorrido( long id, Recorrido recorrido );

}
