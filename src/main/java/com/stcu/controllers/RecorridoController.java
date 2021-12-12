package com.stcu.controllers;

import java.util.List;

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

    @GetMapping("/recorrido/activo/{idlinea}")
    public String findRecorridoActivo( @PathVariable long idlinea ) {
        Recorrido recorrido = service.getRecorridoActivo(idlinea);

        Response<RecorridoDTO> response = new Response<RecorridoDTO>( false,200,"Recorrido activo de linea " + idlinea, new RecorridoDTO( recorrido ));
        return Mapper.getResponseAsJson(response);
    }

    @PostMapping("/recorridos")
    public String saveRecorrido( @RequestBody RecorridoDTO drec ) {
        /*
        Recorrido newRec = service.saveRecorrido( drec.toDTO() );
        Response<ParadaDTO> response;
        if (newParada != null)
            response = new Response<ParadaDTO>( false, 200,"Nueva parada creada", new ParadaDTO(newParada) );
        else
            response = new Response<ParadaDTO>( true, 400,"No se pudo guardar nueva parada", null );
        return Mapper.getResponseAsJson(response);
        */
        return "OK";
    }
}
