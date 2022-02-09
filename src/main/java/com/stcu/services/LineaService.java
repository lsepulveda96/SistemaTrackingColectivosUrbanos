package com.stcu.services;

import java.util.List;
import com.stcu.model.Linea;
import com.stcu.model.Parada;
import com.stcu.model.Recorrido;

public interface LineaService {

    public List<Linea> getAllLineas();

    public Linea getLinea( long id );

    public Linea saveLinea( Linea linea );

    public Linea updateLinea( long id, Linea linea );

    public List<Recorrido> getRecorridos( long idLinea );
    
    public Recorrido getRecorridoActual( long idLinea );

    public List<Parada> getParadas( long idRecorrido ); 
}
