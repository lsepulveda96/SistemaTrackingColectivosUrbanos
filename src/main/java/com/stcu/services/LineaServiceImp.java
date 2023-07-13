package com.stcu.services;

import java.util.List;

import com.stcu.model.Linea;
import com.stcu.repository.LineaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineaServiceImp implements LineaService {

    @Autowired
    private LineaRepository repo;

    @Override
    public List<Linea> getAllLineas() {
        List<Linea> lineas = repo.findAll();
        return lineas;
    }

    @Override
    public Linea getLinea(long id) {
        Linea linea = repo.findById(id);
        return linea;
    }

    @Override
    public Linea saveLinea(Linea linea) {
        return repo.save(linea);
    }

    @Override
    public Linea updateLinea(long id, Linea linea) {
        Linea updLinea = repo.findById(id);
        if (updLinea != null) {
            updLinea.setDenominacion( linea.getDenominacion() );
            updLinea.setDescripcion( linea.getDescripcion() );
            updLinea.setEnServicio( linea.isEnServicio());
            updLinea.setEstado(linea.getEstado());
            repo.save(updLinea);
        }
        return updLinea;
    }

}
