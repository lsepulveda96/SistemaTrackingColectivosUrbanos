package com.stcu.services;

import java.util.List;

import com.stcu.model.Parada;

public interface ParadaService {

    public List<Parada> getAllParadas();

    public Parada getParada( long codigo );

    public Parada saveParada( Parada parada );

    public Parada updateParada( long codigo, Parada parada );
    
}
