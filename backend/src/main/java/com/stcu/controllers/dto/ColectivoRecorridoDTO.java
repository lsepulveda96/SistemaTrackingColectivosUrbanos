package com.stcu.controllers.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.stcu.model.Colectivo;
import com.stcu.model.ColectivoRecorrido;
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

    public ColectivoRecorridoDTO() {
    }

    public ColectivoRecorridoDTO(ColectivoRecorrido cr) {
        this.id = cr.getId();
        this.desde = cr.getDesde();
        this.hasta = cr.getHasta();
        this.transito = cr.isTransito();
        Colectivo col = cr.getColectivo();
        this.colectivo = new Colectivo(col.getId(), col.getUnidad(), col.getPatente());
        this.recorridoId = cr.getRecorrido().getId();
        this.recorridoDenominacion = cr.getRecorrido().getDenominacion();
    }

    public static List<ColectivoRecorridoDTO> toListColectivoRecorridoDTO(List<ColectivoRecorrido> list) {
        List<ColectivoRecorridoDTO> prList = new ArrayList<ColectivoRecorridoDTO>();
        list.forEach(item -> {
            prList.add(toColectivoRecorridoDTO(item));
        });
        return prList;
    }

    private static ColectivoRecorridoDTO toColectivoRecorridoDTO(ColectivoRecorrido cr) {
        ColectivoRecorridoDTO ncr = new ColectivoRecorridoDTO();
        ncr.setId(cr.getId());
        ncr.setDesde(cr.getDesde());
        ncr.setHasta(cr.getHasta());
        ncr.setTransito(cr.isTransito());
        Colectivo col = cr.getColectivo();
        ncr.setColectivo(new Colectivo(col.getId(), col.getUnidad(), col.getPatente()));
        ncr.setRecorridoId(cr.getRecorrido().getId());
        ncr.setRecorridoDenominacion(cr.getRecorrido().getDenominacion());
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
