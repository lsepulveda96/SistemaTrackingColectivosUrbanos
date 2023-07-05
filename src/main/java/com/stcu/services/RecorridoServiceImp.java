package com.stcu.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
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
        return this.recorridoRepo.findAll(idlinea);
    }

    @Override
    public List<Recorrido> getRecorridosActivos(long idlinea) {
        return this.recorridoRepo.findActivos(idlinea);
    }

    @Override
    public List<Recorrido> getRecorridosNoActivos(long idlinea) {
        return this.recorridoRepo.findNoActivos(idlinea);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Recorrido saveRecorrido(Recorrido recorrido) {
        try {
            Recorrido newRec = this.recorridoRepo.save(recorrido);
            for (ParadaRecorrido pr : recorrido.getParadas()) {
                Parada par = this.paradaRepo.findByCodigo(pr.getParada().getCodigo());
                ParadaRecorrido newPR = new ParadaRecorrido(par, newRec);
                newPR.setOrden(pr.getOrden());
                this.paradaRecRepo.save(newPR);
            }
            return newRec;
        } catch (Exception ex) {
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Recorrido updateRecorrido(long id, Recorrido recorrido) {
        try {
            Recorrido rec = this.recorridoRepo.findById(id);
            if (rec != null) {
                rec.setDenominacion(recorrido.getDenominacion());
                rec.setTrayectos(recorrido.getTrayectos());
                rec.setWaypoints(recorrido.getWaypoints());

                this.recorridoRepo.save(rec);

                for (ParadaRecorrido pr : recorrido.getParadas()) {
                    System.out.println("------------------------- recorrido id: " + id + ", paradaRecorrido: " + pr.getId() + ", parada: " + pr.getParada() );
                    if (pr.getId() == 0) { // agregar parada recorrido
                        System.out.println("------------------------- Agregar paradaRecorrido: " + pr.getId() + ", parada: " + pr.getParada() );
                        Parada par = this.paradaRepo.findByCodigo(pr.getParada().getCodigo());
                        ParadaRecorrido newPR = new ParadaRecorrido(par, rec);
                        newPR.setOrden(pr.getOrden());
                        this.paradaRecRepo.save(newPR);
                    } else if (pr.getId() > 0) { // actualizar o eliminar parada recorrido.
                        if (pr.getParada() != null) { // actualizar
                            System.out.println("------------------------- Actualizar paradaRecorrido: " + pr.getId() + ", parada:  "+ pr.getParada() );
                            ParadaRecorrido updPR = this.paradaRecRepo.findById(pr.getId());
                            updPR.setOrden(pr.getOrden());
                            this.paradaRecRepo.save(updPR);
                        } else { // si parada == null se debe eliminar la parada recorrido.
                            System.out.println("------------------------- Eliminar paradaRecorrido: " + pr.getId() + ", parada:  " + pr.getParada() );
                            this.paradaRecRepo.deleteById(pr.getId());
                        }
                    }
                }
            }
            return rec;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<ParadaRecorrido> getParadasRecorrido(long idrec) {
        List<ParadaRecorrido> prs = this.paradaRecRepo.findByRecorridoId(idrec);
        return prs;
    }

    @Override
    public long existDenominacion(long idlinea, String denom) {
        Recorrido rec = this.recorridoRepo.findRecorridoActivoByDenominacion(idlinea, denom);
        return rec != null ? rec.getId() : 0;
    }

    @Override
    public Recorrido deactivateRecorrido(long id) {
        Recorrido rec = this.recorridoRepo.findById(id);
        if (rec != null) {
            rec.setFechaFin(Calendar.getInstance());
            rec.setActivo(false);

            return this.recorridoRepo.save(rec);
        }
        return null;
    }
}
