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

    private static final int VEL_PROMEDIO = 25000;


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
                newPR.setDistancia(pr.getDistancia());
                Double distDoubleAux = Double.valueOf(pr.getDistancia());
                newPR.setTiempo(((distDoubleAux/VEL_PROMEDIO) * 60 * 60));
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
                    if (pr.getId() == 0) { // agregar parada recorrido
                        Parada par = this.paradaRepo.findByCodigo(pr.getParada().getCodigo());
                        ParadaRecorrido newPR = new ParadaRecorrido(par, rec);
                        newPR.setOrden(pr.getOrden());
                        newPR.setDistancia(pr.getDistancia());
                        newPR.setTiempo(pr.getDistancia()/VEL_PROMEDIO);
                        this.paradaRecRepo.save(newPR);
                    } else if (pr.getId() > 0) { // actualizar o eliminar parada recorrido.
                        if (pr.getParada() != null) { // actualizar
                            ParadaRecorrido updPR = this.paradaRecRepo.findById(pr.getId());
                            updPR.setOrden(pr.getOrden());
                            this.paradaRecRepo.save(updPR);
                        } else { // si parada == null se debe eliminar la parada recorrido.
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

    //para App colectivo

    @Override
    public List<Recorrido> getRecorridosActivosByDenomLinea(String denomLinea) {
        return this.recorridoRepo.findActivosByDenomLinea(denomLinea);
    }

    // este esta causando error. reemplazarlo por otro
    @Override
    public Recorrido getRecorridoByDenom(String denomRecorrido){
        return this.recorridoRepo.findRecorridoByDenom(denomRecorrido);
    }

    @Override  
    public List<ParadaRecorrido> getParadasRecorridoByLineaDenomYRecorridoDenom( String lineaDenom, String recorridoDenom ){
               List<ParadaRecorrido> prs = this.paradaRecRepo.findParadasRecorridoByLineaDenomYRecorridoDenom(lineaDenom,recorridoDenom);
        return prs;
    }

    @Override  
    public Recorrido getRecorridoByLineaDenomYRecorridoDenom(String denomLinea, String denomRecorrido){
        return this.recorridoRepo.findRecorridoByLineaDenomYRecorridoDenom(denomLinea,denomRecorrido);
    }

    public Boolean verificarUnidadEnRecorrido(String latitud, String longitud, long idRec) {
        return this.recorridoRepo.verificarUnidadEnRecorrido(latitud,longitud,idRec);
    }

    // Para app Pasajero
    @Override
    public List<Recorrido> getRecorridosActivosByIdLinea(int idLinea) {
        return this.recorridoRepo.findActivosByIdLinea(idLinea);
    }
    

    // Para app Pasajero
    @Override  
    public List<ParadaRecorrido> getParadasRecorridoByLineaIdYRecorridoId( long idLinea, long idRecorrido ){
               List<ParadaRecorrido> prs = this.paradaRecRepo.getParadasRecorridoByLineaIdYRecorridoId(idLinea,idRecorrido);
        return prs;
    }


    // para traer todas paradas app pasajero (recorridos activos)
    // Para app Pasajero
    @Override  
    public List<ParadaRecorrido> getParadasRecorridoByLineaDenom( String lineaDenom ){
               List<ParadaRecorrido> prs = this.paradaRecRepo.getParadasRecorridoByLineaDenom(lineaDenom);
        return prs;
    }

}
