package com.stcu.services;

import java.util.List;

import com.stcu.model.Colectivo;
import com.stcu.model.Documento;

public interface ColectivoService {
    
    public List<Colectivo> getAllColectivos();

    public Colectivo getColectivo( long id );

    public Colectivo saveColectivo( Colectivo col );

    public Colectivo updateColectivo( long id, Colectivo col );

    public boolean bajaColectivo( long id );

    public Colectivo getColectivoByUnidad( String unidad );

    public List<Colectivo> getAllColectivosSinCircular();

    public Colectivo saveImage( long id, String path );

    public Documento getDocumento( long id );
    
    public Documento saveDocumento( long id, Documento doc);

    public Documento updateDocumento( long id, Documento doc );

    public boolean removeDocumento( long id );

    public List<Documento> getDocsVencidosProximoVencer();

    public List<Colectivo> getColectivosDocsVencimiento();

}
