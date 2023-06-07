package com.stcu.controllers;

import java.util.List;

import com.stcu.controllers.dto.CoordenadaDTO;
import com.stcu.controllers.dto.RecorridoDTO;
import com.stcu.model.Recorrido;
import com.stcu.services.RecorridoServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecorridoController {
    
    @Autowired
    RecorridoServiceImp service;

    @GetMapping("/recorridos/{idlinea}")
    public String findAllRecorridos( @PathVariable long idlinea ) {
        List<Recorrido> recorridos = service.getRecorridosLinea(idlinea);

        Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>( false,200, "Recorridos de linea " + idlinea, RecorridoDTO.toListRecorridosDTO(recorridos));
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/recorridos/activo/{idlinea}")
    public String findRecorridosActivos( @PathVariable long idlinea ) {
        List<Recorrido> recorridos = service.getRecorridosActivos(idlinea);

        Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>( false,200,"Recorridos activos de linea " + idlinea, RecorridoDTO.toListRecorridosDTO(recorridos));
        return Mapper.getResponseAsJson(response);
    }

    @PostMapping("/recorridos")
    public String saveRecorrido( @RequestBody RecorridoDTO drec ) {
        System.out.println("save Recorrido controller: " );
        System.out.println(" DTO Recorrido linea id: " + drec.getLinea().getId());
        System.out.println(" DTO Recorrido id: " + drec.getId());
        System.out.println(" DTO Recorrido denominacion: " + drec.getDenominacion());
        System.out.println(" DTO Recorrido fecha inicio: " + drec.getFechaInicio());
        System.out.println(" DTO Recorrido fecha fin: " + drec.getFechaFin());
        System.out.println(" DTO Recorrido es activo: " + drec.isActivo());
        
        for (CoordenadaDTO wp: drec.getWaypoints())
            System.out.println(" waypoint: lat " + wp.getLat() + " lng " + wp.getLng() );

        for (CoordenadaDTO wp: drec.getTrayectos())
            System.out.println(" trayecto: lat " + wp.getLat() + " lng " + wp.getLng() );

        Recorrido newRec = service.saveRecorrido( drec.toRecorrido() );
        Response<RecorridoDTO> response;
        if (newRec != null)
            response = new Response<RecorridoDTO>( false, 200,"Nuevo Recorrido creado", new RecorridoDTO(newRec) );
        else
            response = new Response<RecorridoDTO>( true, 400,"No se pudo guardar nuevo Recorrido", null );
        return Mapper.getResponseAsJson(response);
    }
}
