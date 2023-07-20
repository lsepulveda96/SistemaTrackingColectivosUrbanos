package com.stcu.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.stcu.controllers.dto.ColectivoRecorridoDTO;
import com.stcu.model.ColectivoRecorrido;
import com.stcu.services.MonitorService;

@RestController
public class MonitorController {

    @Autowired
    private MonitorService service;

    @GetMapping("/transito/unidades")
    public String findColectivosTransito() {
        List<ColectivoRecorrido> list = service.getColectivosTransito();
        Response<List<ColectivoRecorridoDTO>> response = new Response<List<ColectivoRecorridoDTO>>(false, 200,
                "Colectivo en Recorrido ", ColectivoRecorridoDTO.toListColectivoRecorridoDTO(list));
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/transito/unidad/{id}")
    public String findColectivoRecorridoTransito(@PathVariable long id) {
        ColectivoRecorrido colRec = this.service.getColectivoRecorrido(id);

        Response<ColectivoRecorridoDTO> response;
        if (colRec != null)
            response = new Response<ColectivoRecorridoDTO>(false, 200, "Colectivo recorrido",
                    new ColectivoRecorridoDTO(colRec));
        else
            response = new Response<ColectivoRecorridoDTO>(true, 400, "No se encontro colectivo recorrido", null);
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/transito/ubicacion/{id}")
    public String findUltimaUbicacionTransito( @PathVariable long id ) {
        return "";
    }
}
