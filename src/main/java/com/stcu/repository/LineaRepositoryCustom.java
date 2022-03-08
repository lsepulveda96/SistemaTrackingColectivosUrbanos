package com.stcu.repository;

import java.util.List;

import com.stcu.model.Parada;
import com.stcu.model.Recorrido;

public interface LineaRepositoryCustom {

    /**
     * Find all recorridos of a linea
     * @return
     */
    List<Recorrido> findAllRecorridos( long id );

    /**
     * find Recorrido actual of linea
     * @return
     */
    Recorrido findRecorridoActual( long id );

    /**
     * find paradas list of linea.
     * @return
     */
    List<Parada> findParadas( long id );
    
}
