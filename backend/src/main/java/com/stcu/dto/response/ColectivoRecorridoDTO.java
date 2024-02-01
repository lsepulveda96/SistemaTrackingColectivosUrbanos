package com.stcu.dto.response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.stcu.model.Colectivo;
import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Parada;
import com.stcu.model.Recorrido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColectivoRecorridoDTO {

    private long id;
    private Calendar desde;
    private Calendar hasta;
    private boolean transito;
    private Colectivo colectivo;
    private long recorridoId;
    private String recorridoDenominacion;

    //para mostrar en pantalla monitoreo
    private String lineaDenominacion;
    private long lineaId;
    private List<CoordenadaDTO> coordenadas;
    private Calendar fechaParadaActual;

    //para App colectivo
    private Parada paradaActual;

    //para monitoreo
    private CoordenadaDTO ultimaUbicacion;

    public ColectivoRecorridoDTO() {
    }

    public ColectivoRecorridoDTO(ColectivoRecorrido cr) {
        this.id = cr.getId();
        this.desde = cr.getDesde();
        this.hasta = cr.getHasta();
        this.transito = cr.isTransito();
        this.fechaParadaActual = cr.getFechaParadaActual();
        Colectivo col = cr.getColectivo();
        if (col != null )
            this.colectivo = new Colectivo(col.getId(), col.getUnidad(), col.getPatente());
        Recorrido rec = cr.getRecorrido();
        if (rec != null) {
            this.recorridoId = rec.getId();
            this.recorridoDenominacion = rec.getDenominacion();
            if (rec.getLinea() != null) {
                this.lineaDenominacion = rec.getLinea().getDenominacion();
                this.lineaId = rec.getLinea().getId();
            }
        }
        Parada par =cr.getParadaActual();
        if (par != null)
            this.paradaActual = new Parada(par.getCodigo(),par.getDireccion(),par.getCoordenadas());
    }

    public static List<ColectivoRecorridoDTO> toListColectivoRecorridoDTO(List<ColectivoRecorrido> list) {
        List<ColectivoRecorridoDTO> prList = new ArrayList<ColectivoRecorridoDTO>();
        list.forEach(item -> {
            prList.add(toColectivoRecorridoDTO(item));
        });
        return prList;
    }

    public static ColectivoRecorridoDTO toColectivoRecorridoDTO(ColectivoRecorrido cr) {
        ColectivoRecorridoDTO ncr = new ColectivoRecorridoDTO();
        ncr.setId(cr.getId());
        ncr.setDesde(cr.getDesde());
        ncr.setHasta(cr.getHasta());
        ncr.setTransito(cr.isTransito());
        Colectivo col = cr.getColectivo();
        ncr.setColectivo(new Colectivo(col.getId(), col.getUnidad(), col.getPatente()));
        ncr.setRecorridoId(cr.getRecorrido().getId());
        ncr.setRecorridoDenominacion(cr.getRecorrido().getDenominacion());
        ncr.setLineaDenominacion(cr.getRecorrido().getLinea().getDenominacion());
        ncr.setLineaId(cr.getRecorrido().getLinea().getId());
        ncr.setFechaParadaActual(cr.getFechaParadaActual());
        Parada par =cr.getParadaActual();
        if(par != null)
            ncr.setParadaActual(new Parada(par.getCodigo(),par.getDireccion(),par.getCoordenadas()));
        return ncr;
    }

    public static List<ColectivoRecorrido> toListColectivoRecorrido(List<ColectivoRecorridoDTO> listdto) {
        List<ColectivoRecorrido> listCR = new ArrayList<ColectivoRecorrido>();
        listdto.forEach(item -> {
            Colectivo col = new Colectivo(item.getColectivo().getId(), item.getColectivo().getUnidad(),
                    item.getColectivo().getPatente());
            Recorrido rec = new Recorrido();
            rec.setId(item.getRecorridoId());
            rec.setDenominacion(item.getRecorridoDenominacion());
            ColectivoRecorrido cr = new ColectivoRecorrido(col, rec);
            cr.setId(item.getId());
            cr.setDesde(item.getDesde());
            cr.setHasta(item.getHasta());
            cr.setTransito(item.isTransito());

            listCR.add(cr);
        });
        return listCR;
    }
}
