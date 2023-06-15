package com.stcu.controllers.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.stcu.model.Parada;
import com.stcu.model.ParadaRecorrido;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ParadaRecorridoDTO implements Serializable {
    
    private long id;
    private long paradaCodigo;

    private int orden;
    private int distancia;
    private double tiempo;

    public ParadaRecorridoDTO() {}

    public ParadaRecorridoDTO( ParadaRecorrido pr ) {
        this.id = pr.getId();
        this.paradaCodigo = pr.getParada().getCodigo();
        //this.recorridoId = pr.getRecorrido().getId();
        this.orden = pr.getOrden();
        this.distancia = pr.getDistancia();
        this.tiempo = pr.getTiempo();
    }

    public static List<ParadaRecorridoDTO> toListParadaRecorridoDTO( List<ParadaRecorrido> list ) {
        List<ParadaRecorridoDTO> prList = new ArrayList<ParadaRecorridoDTO>();
        list.forEach( item -> {
            prList.add( toParadaRecorridoDTO(item));
        });
        return prList;
    }

    private static ParadaRecorridoDTO toParadaRecorridoDTO( ParadaRecorrido pr ) {
        ParadaRecorridoDTO npr = new ParadaRecorridoDTO();
        npr.setId( pr.getId() );
        npr.setParadaCodigo( pr.getParada().getCodigo() );
        //npr.setRecorridoId( pr.getRecorrido().getId() );
        npr.setOrden( pr.getOrden() );
        npr.setDistancia( pr.getDistancia() );
        npr.setTiempo( pr.getDistancia() );

        return npr;
    }

    public static List<ParadaRecorrido> toListParadaRecorrido( List<ParadaRecorridoDTO> listdto ) {
        List<ParadaRecorrido> listPR = new ArrayList<ParadaRecorrido>();
        listdto.forEach( item -> {
            ParadaRecorrido pr = new ParadaRecorrido( new Parada( item.getParadaCodigo(), null, null), null );
            pr.setOrden( item.getOrden() );
            listPR.add( pr );
        });
        return listPR;
    }
}
