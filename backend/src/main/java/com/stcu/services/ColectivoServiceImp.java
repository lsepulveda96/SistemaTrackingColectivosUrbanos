package com.stcu.services;

import java.util.List;

import com.stcu.model.Colectivo;
import com.stcu.repository.ColectivoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ColectivoServiceImp implements ColectivoService {

    @Autowired
    private ColectivoRepository rep;

    @Override
    public List<Colectivo> getAllColectivos() {
        List<Colectivo> cols = this.rep.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return cols;
    }

    @Override
    public Colectivo getColectivo(long id) {

        return this.rep.findById(id);
    }

    @Override
    public Colectivo saveColectivo(Colectivo col) {
        try {
            return this.rep.save(col);
        } catch (Exception ex) {
            System.out.println("++++++ ERROR guardano nuevo colectivo: " + ex);
            return null;
        }
    }

    @Override
    public Colectivo updateColectivo(long id, Colectivo col) {
        try {
            Colectivo colectivo = this.rep.findById(id);
            if (colectivo != null) {
                colectivo.setUnidad(col.getUnidad());
                colectivo.setMarca(col.getMarca());
                colectivo.setModelo(col.getModelo());
                colectivo.setCapacidad(col.getCapacidad());
                colectivo.setAnio(col.getAnio());
                colectivo.setPatente(col.getPatente());
                colectivo.setFechaCompra(col.getFechaCompra());
                colectivo.setEstado(col.getEstado());
                colectivo.setImgpath(col.getImgpath());
                return this.rep.save(colectivo);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("++++++ ERROR actualizando colectivo " + ex);
            return null;
        }
    }

    @Override
    public boolean bajaColectivo(long id) {
        Colectivo col = this.rep.findById(id);
        if (col != null) {
            col.setEstado("BAJA");
            this.rep.save(col);
            return true;
        }
        return false;
    }

}