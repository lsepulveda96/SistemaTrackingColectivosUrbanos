package com.stcu.services;

import java.util.List;

import com.stcu.model.Colectivo;
import com.stcu.repository.ColectivoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColectivoServiceImp implements ColectivoService {

    @Autowired
    private ColectivoRepository rep;

    @Override
    public List<Colectivo> getAllColectivos() {
        List<Colectivo> cols = this.rep.findAll();
        return cols;
    }

    @Override
    public Colectivo getColectivo( long id) {
    
        return this.rep.findById(id);
    }

    @Override
    public Colectivo saveColectivo(Colectivo col) {
        return this.rep.save( col );
    }

    @Override
    public Colectivo updateColectivo(long id, Colectivo col) {
        Colectivo colectivo = this.rep.findById(id);
        if (colectivo != null) {
            colectivo.setUnidad( col.getUnidad() );
            colectivo.setMarca( col.getMarca());
            colectivo.setModelo( col.getModelo() );
            colectivo.setCapacidad(col.getCapacidad() );
            colectivo.setAnio( col.getAnio() );
            colectivo.setPatente( col.getPatente() );
            colectivo.setFechaCompra( col.getFechaCompra() );
            colectivo.setEstado( col.getEstado());
            return this.rep.save(colectivo);
        }
        return null;
    }
    
}