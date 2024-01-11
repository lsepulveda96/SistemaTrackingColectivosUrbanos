package com.stcu.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stcu.dto.response.ColectivoRecorridoDTO;
import com.stcu.dto.response.CoordenadaDTO;
import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Ubicacion;
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
     */


    /**
     * Busca y retorna la ultima ubicacion registrada de un colectivo en transito.
     * 
     * @param id
     * @return
     
    @GetMapping("/transito/ubicacion/{id}")
    public String findUltimaUbicacionTransito(@PathVariable long id) {
        log.info("*** findUltimaUbicacionTransito");
        return "";
    }
    */


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


     @GetMapping("/transito/coordenadas/unidad/{id}")
    public String getCoordenadasColectivosTransito(@PathVariable long id) {

        Ubicacion coordColeRec = this.service.getLastUbicacion(id);    

        Response<CoordenadaDTO> response;
        if (coordColeRec != null){
            response = new Response<CoordenadaDTO>(false, 200, "Coordenada colectivo recorrido",
                    new CoordenadaDTO(coordColeRec.getCoordenada().getX(),coordColeRec.getCoordenada().getY()));
                    System.out.println("+++++++++++++++++++++++ Coordenada colectivo recorrido");
                    }
        else{
            response = new Response<CoordenadaDTO>(true, 400, "No se encontraron coordenadas colectivo recorrido", null);
            System.out.println("+++++++++++++++++++++++ No se encontraron coordenadas colectivo recorrido");
        }
        return Mapper.getResponseAsJson(response);
    }
}
