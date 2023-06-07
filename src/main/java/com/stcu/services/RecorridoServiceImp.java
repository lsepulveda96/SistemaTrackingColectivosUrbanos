package com.stcu.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.stcu.model.Recorrido;
import com.stcu.repository.RecorridoRepository;

@Service
public class RecorridoServiceImp implements RecorridoService {
    
    @Autowired
    private RecorridoRepository recorridoRepo;

    @Override
    public Recorrido getRecorrido(long id) {
        return this.recorridoRepo.findById(id);
    }

    @Override
    public List<Recorrido> getRecorridosLinea(long idlinea) {
        return this.recorridoRepo.findAll( idlinea );
    }

    @Override
    public List<Recorrido> getRecorridosActivos( long idlinea ) {
        return this.recorridoRepo.findActivos(idlinea);
    }

    @Override
    public Recorrido saveRecorrido(Recorrido recorrido) {
        return this.recorridoRepo.save( recorrido );
    }

    @Override
    public Recorrido updateRecorrido(long id, Recorrido recorrido) {
        Recorrido rec = this.recorridoRepo.findById(id);
        if (rec != null) {
            rec.setDenominacion( recorrido.getDenominacion());
            rec.setFechaInicio( recorrido.getFechaInicio());
            rec.setFechaFin(recorrido.getFechaFin());
            rec.setActivo( recorrido.isActivo());
            rec.setTrayectos( recorrido.getTrayectos());
            rec.setWaypoints( recorrido.getWaypoints() );
            
            return this.recorridoRepo.save( rec );
        }
        return null;
    }
}
