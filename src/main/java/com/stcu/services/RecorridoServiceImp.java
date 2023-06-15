package com.stcu.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.stcu.model.Parada;
import com.stcu.model.ParadaRecorrido;
import com.stcu.model.Recorrido;
import com.stcu.repository.ParadaRecorridoRepository;
import com.stcu.repository.ParadaRepository;
import com.stcu.repository.RecorridoRepository;

@Service
public class RecorridoServiceImp implements RecorridoService {
    
    @Autowired
    private RecorridoRepository recorridoRepo;
    @Autowired
    private ParadaRecorridoRepository paradaRecRepo;
    @Autowired
    private ParadaRepository paradaRepo;
    
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Recorrido saveRecorrido(Recorrido recorrido) {
        try {
            Recorrido newRec = this.recorridoRepo.save( recorrido );
            for (ParadaRecorrido pr: recorrido.getParadas()) {
                Parada par = this.paradaRepo.findByCodigo( pr.getParada().getCodigo() );
                ParadaRecorrido newPR = new ParadaRecorrido( par, newRec );
                newPR.setOrden( pr.getOrden());
                this.paradaRecRepo.save( newPR );
            }
            return newRec;
        }
        catch( Exception ex ) {
            return null;
        }
        
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
