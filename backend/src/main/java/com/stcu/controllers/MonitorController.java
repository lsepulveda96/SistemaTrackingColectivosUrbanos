package com.stcu.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stcu.dto.response.ColectivoRecorridoDTO;
import com.stcu.model.ColectivoRecorrido;
import com.stcu.services.MonitorService;

@RestController
@RequestMapping("/api")
public class MonitorController {

    @Autowired
    private MonitorService service;

    private static final Logger log = Logger.getLogger(MonitorController.class.getName());

    /**
     * Busca la lista de colectivos en trasito en el momento.
     * 
     * @return
     */
    @GetMapping("/transito/unidades")
    public String findColectivosTransito() {
        log.info("*** findColectivosTransito ");
        List<ColectivoRecorrido> list = service.getColectivosTransito();
        log.info("*** Colectivos en transito: " + list.size());
        Response<List<ColectivoRecorridoDTO>> response = new Response<List<ColectivoRecorridoDTO>>(false, 200,
                "Colectivo en Recorrido ", ColectivoRecorridoDTO.toListColectivoRecorridoDTO(list));
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Busca y recupera un colectivo en transito, a partir de su id.
     * 
     * @param id
     * @return
     */
    @GetMapping("/transito/unidad/{id}")
    public String findColectivoRecorridoTransito(@PathVariable long id) {
        log.info("*** findColectivoRecorridoTransito");
        ColectivoRecorrido colRec = this.service.getColectivoRecorrido(id);

        Response<ColectivoRecorridoDTO> response;
        if (colRec != null) {
            log.info("*** Colectivo en transito: " + colRec.getColectivo().getUnidad());
            response = new Response<ColectivoRecorridoDTO>(false, 200, "Colectivo recorrido",
                    new ColectivoRecorridoDTO(colRec));
        } else {
            log.info("*** No se encontro colectivo en transito " + id);
            response = new Response<ColectivoRecorridoDTO>(true, 400, "No se encontro colectivo recorrido", null);
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Busca y retorna la ultima ubicacion registrada de un colectivo en transito.
     * 
     * @param id
     * @return
     */
    @GetMapping("/transito/ubicacion/{id}")
    public String findUltimaUbicacionTransito(@PathVariable long id) {
        log.info("*** findUltimaUbicacionTransito");
        return "";
    }
}
