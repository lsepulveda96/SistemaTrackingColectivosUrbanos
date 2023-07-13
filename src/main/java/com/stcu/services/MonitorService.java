package com.stcu.services;

import java.util.List;

import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Ubicacion;

public interface MonitorService {
    
    public List<ColectivoRecorrido> getColectivosTransito();

    public ColectivoRecorrido getColectivoRecorrido( long idcr );

    public ColectivoRecorrido saveColectivoRecorrido( ColectivoRecorrido cr );

    public Ubicacion saveUbicacion( Ubicacion ubicacion );

    public Ubicacion getLastUbicacion( long cr );

    public List<Ubicacion> findUbicaciones( long cr );
}
