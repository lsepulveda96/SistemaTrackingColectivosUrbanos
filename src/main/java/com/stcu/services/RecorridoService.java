package com.stcu.services;

import java.util.List;

import com.stcu.model.ParadaRecorrido;
import com.stcu.model.Recorrido;

public interface RecorridoService {
    
    public List<Recorrido> getRecorridosLinea( long idlinea );

    public Recorrido getRecorrido( long id );

    public List<Recorrido> getRecorridosActivos( long idlinea );

    public Recorrido saveRecorrido( Recorrido recorrido );

    public Recorrido updateRecorrido( long id, Recorrido recorrido );

    public List<ParadaRecorrido> getParadasRecorrido( long idrecorrido );

    public boolean existDenominacion( long idlinea, String denom );
}
