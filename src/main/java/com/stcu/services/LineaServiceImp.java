package com.stcu.services;

import java.util.List;

import com.stcu.model.Linea;
import com.stcu.model.Parada;
import com.stcu.model.Recorrido;
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
            updLinea.setEnServicio( linea.isEnServicio());
            updLinea.setEstado(linea.getEstado());
            repo.save(updLinea);
        }
        return updLinea;
    }

    @Override
    public List<Recorrido> getRecorridos(long idLinea) {
        return repo.findAllRecorridos( idLinea );
    }

    @Override
    public Recorrido getRecorridoActual(long idLinea) {
        return  repo.findRecorridoActual( idLinea );
    }

    @Override
    public List<Parada> getParadas(long idRecorrido) {
        return repo.findParadas(idRecorrido);
    }
    

}
