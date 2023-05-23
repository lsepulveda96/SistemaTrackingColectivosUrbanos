package com.stcu.services;

import java.util.List;

import com.stcu.model.Parada;

public interface ParadaService {

    public List<Parada> getAllParadas();

    public List<Parada> getAllParadasActivas();

    public Parada getParada( long codigo );

    public Parada saveParada( Parada parada );

    public Parada updateParada( long codigo, Parada parada );
    
    public Parada enableDisableParada( long codigo, boolean disable );
}
