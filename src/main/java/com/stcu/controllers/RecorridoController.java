package com.stcu.controllers;

import java.util.List;

import com.stcu.controllers.dto.ParadaRecorridoDTO;
import com.stcu.controllers.dto.RecorridoDTO;
import com.stcu.model.ParadaRecorrido;
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
    public String findAllRecorridos(@PathVariable long idlinea) {
        List<Recorrido> recorridos = service.getRecorridosLinea(idlinea);

        Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>(false, 200,
                "Recorridos de linea " + idlinea, RecorridoDTO.toListRecorridosDTO(recorridos));
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/recorridos/activo/{idlinea}")
    public String findRecorridosActivos(@PathVariable long idlinea) {
        List<Recorrido> recorridos = service.getRecorridosActivos(idlinea);

        Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>(false, 200,
                "Recorridos activos de linea " + idlinea, RecorridoDTO.toListRecorridosDTO(recorridos));
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/recorrido/paradas/{idrecorrido}")
    public String findParadasRecorrido(@PathVariable long idrecorrido) {
        List<ParadaRecorrido> paradas = service.getParadasRecorrido(idrecorrido);

        Response<List<ParadaRecorridoDTO>> response;
        if (paradas != null)
            response = new Response<List<ParadaRecorridoDTO>>(false, 200, "Paradas de recorrido " + idrecorrido,
                    ParadaRecorridoDTO.toListParadaRecorridoDTO(paradas));
        else
            response = new Response<List<ParadaRecorridoDTO>>(true, 400,
                    "No se pudo recuperar lista de paradas de recorrido " + idrecorrido, null);
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/recorrido/{id}")
    public String findRecorrido( @PathVariable long id ) {
        Recorrido recorrido = service.getRecorrido(id);
        Response<RecorridoDTO> response;
        if (recorrido != null) 
            response = new Response<RecorridoDTO>(false, 200, "Recorrido " + id , new RecorridoDTO(recorrido));
        else
            response = new Response<RecorridoDTO>( true, 400, "No se encontro recorrido " + id, null );
        return Mapper.getResponseAsJson(response);
    }

    @PostMapping("/recorridos")
    public String saveRecorrido(@RequestBody RecorridoDTO drec) {
        Response<RecorridoDTO> response;
        if (service.existDenominacion(drec.getLinea().getId(), drec.getDenominacion())) {
            response = new Response<RecorridoDTO>(true, 400, "Ya existe recorrido activo con igual denominacion, ingrese otro valor en el campo denominacion", null);
        } else {
            Recorrido newRec = service.saveRecorrido(drec.toRecorrido());
            if (newRec != null)
                response = new Response<RecorridoDTO>(false, 200, "Nuevo Recorrido creado", new RecorridoDTO(newRec));
            else
                response = new Response<RecorridoDTO>(true, 400, "No se pudo guardar nuevo Recorrido", null);
        }
        return Mapper.getResponseAsJson(response);
    }
}
