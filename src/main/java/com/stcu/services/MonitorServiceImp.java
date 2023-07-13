package com.stcu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Ubicacion;
import com.stcu.repository.ColectivoRecorridoRepository;
import com.stcu.repository.UbicacionRepository;

public class MonitorServiceImp implements MonitorService {

    @Autowired
    private ColectivoRecorridoRepository repoColRec;
    @Autowired
    private UbicacionRepository repoUbicacion;

    @Override
    public List<ColectivoRecorrido> getColectivosTransito() {
        return this.repoColRec.findByTransitoTrue();
    }

    @Override
    public ColectivoRecorrido saveColectivoRecorrido(ColectivoRecorrido cr) {
        return this.repoColRec.save( cr );
    }

    @Override
    public Ubicacion saveUbicacion( Ubicacion ubicacion ) {
        System.out.println("******************SAVE UBICACION: " + ubicacion.toString() );
        return this.repoUbicacion.save(ubicacion);
    }

    @Override
    public Ubicacion getLastUbicacion(long cr) {
        System.out.println("****************** GET LAST UBICACION... ");
        return this.repoUbicacion.findLastByColectivoRecorrido( cr );
    }

    @Override
    public List<Ubicacion> findUbicaciones(long cr) {
        return this.repoUbicacion.findByColectivoRecorrido( cr );
    }

    @Override
    public ColectivoRecorrido getColectivoRecorrido(long idcr) {
        return this.repoColRec.findById( idcr );
    }

    
}
