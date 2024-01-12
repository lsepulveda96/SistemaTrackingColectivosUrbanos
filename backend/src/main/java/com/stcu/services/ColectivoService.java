package com.stcu.services;

import java.util.List;

import com.stcu.model.Colectivo;

public interface ColectivoService {
    
    public List<Colectivo> getAllColectivos();

    public Colectivo getColectivo( long id );

    public Colectivo saveColectivo( Colectivo col );

    public Colectivo updateColectivo( long id, Colectivo col );

    public boolean bajaColectivo( long id );

    public Colectivo getColectivoByUnidad( String unidad );

    public List<Colectivo> getAllColectivosSinCircular();
}
