package com.stcu.services;

import java.util.List;

import com.stcu.model.Parada;
import com.stcu.repository.ParadaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParadaServiceImp implements ParadaService {
    
    @Autowired
    private ParadaRepository repo;

    @Override
    public List<Parada> getAllParadas() {
        List<Parada> paradas = repo.findAll();
        return paradas;
    }

    @Override
    public Parada getParada(long codigo) {
        return repo.findByCodigo(codigo);
    }

    @Override
    public Parada saveParada( Parada parada) {
        return repo.save(parada);
    }

    @Override
    public Parada updateParada(long codigo, Parada parada) {
        Parada par = repo.findByCodigo(codigo);
        if (par != null) {
            par.setDireccion( parada.getDireccion() );
            par.setDescripcion( parada.getDescripcion());
            par.setCoordenadas( parada.getCoordenadas() );
            return repo.save(par);
        }
        return par;
    }

    @Override
    public Parada disableParada(long codigo) {
        Parada par = repo.findByCodigo(codigo);
        if (par != null) {
            par.setEstado("NO_ACTIVA");
            repo.save( par );
        }
        return par;
    }

    @Override
    public List<Parada> getAllParadasActivas() {
        List<Parada> paradas = repo.findAllActivas();
        return paradas;
    }

}
