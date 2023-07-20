package com.stcu.services;

import java.util.List;

import com.stcu.model.Parada;
import com.stcu.repository.ParadaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ParadaServiceImp implements ParadaService {
    
    @Autowired
    private ParadaRepository repo;

    @Override
    public List<Parada> getAllParadas() {
        List<Parada> paradas = repo.findAll( Sort.by(Sort.Direction.ASC,"codigo"));
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
    public Parada enableDisableParada(long codigo, boolean disabled ) {
        Parada par = repo.findByCodigo(codigo);
        if (par != null) {
            String stat = disabled ? "NO_ACTIVA":"HABILITADA";
            par.setEstado(stat);
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
