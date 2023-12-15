package com.stcu.controllers;

import java.util.List;
import java.util.logging.Logger;

import com.stcu.controllers.dto.RecorridoDTO;
import com.stcu.model.Linea;
import com.stcu.model.Recorrido;
import com.stcu.services.LineaServiceImp;
import com.stcu.services.RecorridoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class LineaController {

    @Autowired
    LineaServiceImp service;
    
    @Autowired
    private RecorridoService recService;

    private static final Logger log = Logger.getLogger(LineaController.class.getName());

    /**
     * Recupera todas las lineas 
     * @return json arreglo de lineas
     */
    @GetMapping("/lineas")
    public String findAllLineas() {
        log.info("*** findAllLineas");
        List<Linea> lineas = service.getAllLineas();
        log.info("*** lineas length: " + lineas.size() );
        Response<List<Linea>> response = new Response<List<Linea>>( false,200,"Lineas", lineas );
        return Mapper.getResponseAsJson(response);
    }

    /**
     * REcupera una linea por su id
     * @param id
     * @return json de respuesta con objecto linea si se encontro.
     */
    @GetMapping("/linea/{id}")
    public String findLinea( @PathVariable long id ){
        log.info("*** findLinea: " + id );
        Linea linea = service.getLinea( id );
        Response<Linea> response;
        if (linea != null) {
            log.info("*** Linea recuperada " + linea.getDenominacion() );
            response = new Response<Linea>( false,200,"Linea " + id, linea );
        }else{
            log.info("*** No se encontrol linea " + id );
            response = new Response<Linea>( true,400,"No se encontro linea " + id, null );
        }
        return Mapper.getResponseAsJson(response);
    }    

    /**
     * Registra una nueva linea-
     * @param linea
     * @return json respuesta con objeto linea creado.
     */
    @PostMapping("/lineas")
    public String saveLinea( @RequestBody Linea linea ) {
        log.info("*** saveLinea : " + linea.getDenominacion() );
        Response<Linea> response;
        try {
            Linea nlinea = service.saveLinea(linea);
            if (nlinea != null) {
                log.info("*** Nueva linea registrada");
                response = new Response<Linea>( false,200,"Nueva linea registrada", nlinea );
            }
            else {    
                log.info("*** No se pudo registrar nueva linea ");
                response = new Response<Linea>( true,400,"No se pudo registrar nueva Linea", null );
            }
        }
        catch( org.hibernate.exception.ConstraintViolationException ex ) {
            log.warning("*** Error registrando nueva linea , ex: " + ex);
            response = new Response<Linea>( true,400,"Ya existe Linea con misma denominacion", null );
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Actualiza una linea por su id.
     * @param id
     * @param linea
     * @return json respuesta con el objeto linea actualizado.
     */
    @PutMapping("/linea/{id}")
    public String updateLinea( @PathVariable long id, @RequestBody Linea linea ) {
        log.info("*** updateLinea : " + linea.getDenominacion() );
        Linea ulinea = service.updateLinea(id, linea);
        Response<Linea> response;
        if (ulinea != null) {
            log.info("*** Linea actualizada");
            response = new Response<Linea>( false, 200, "Linea " + ulinea.getDenominacion() + " actualizada", ulinea );
        }
        else {
            log.info("*** No se pudo actualizar Linea");
            response = new Response<Linea>( true, 400, "No se pudo actualizar Linea " + id, null );
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Recupera todas las paradas del recorrido activo de una linea.
     * @param id
     * @return json respuesta con objeto Lista de paradas
     */
    /* 
    @GetMapping(value="/linea/paradas/{id}")
    public String getParadasLinea(@PathVariable long id ) {
        Recorrido recorridoActual = service.getRecorridoActual( id );
        Response<List<Parada>> response;

        if (recorridoActual != null) {
            List<Parada> paradasRecorrido = service.getParadas( recorridoActual.getId() );
            response = new Response<List<Parada>>( false, 200, "Paradas de recorrido " + recorridoActual.getId(), paradasRecorrido );
        }
        else {
            response = new Response<List<Parada>>( true, 400, "No se encontro recorrido actual para linea " + id, null );
        }
        return Mapper.getResponseAsJson(response);
    }
     */

    /**
     * Recupera lista de recorridos de una linea (pasados y actual)
     * @param id
     * @return json respuesta con objeto Lista de recorridos.
     */
    /* 
    @GetMapping(value = "/linea/recorridos/{id}")
    public String getRecorridosLinea( @PathVariable long id ) {
        List<Recorrido> recorridos = service.getRecorridos( id );

        Response<List<Recorrido>> response = new Response<List<Recorrido>>( false, 200, "Recorridos de linea " + id, recorridos );
        return Mapper.getResponseAsJson(response);
     }
     */

     /* 
     @GetMapping(value="/linea/recorrido/activo/{idlinea}")
     public String getRecorridoActivo(@PathVariable long idlinea) {
        Recorrido recorrido = service.getRecorridoActual(idlinea);
        Response<RecorridoDTO> resp;

        if (recorrido != null) 
            resp = new Response<RecorridoDTO>( false, 200, "Recorrido actual de linea: " + idlinea, new RecorridoDTO( recorrido ) );
        else
            resp = new Response<RecorridoDTO>( true, 300, "No se encontro recorrido para linea " + idlinea, null );

        return Mapper.getResponseAsJson(resp);
     } 
     */  

     @GetMapping( value="/linea/recorrido/{idrecorrido}")
     public String getRecorrido( @PathVariable long idrecorrido ) {
        log.info("*** getRecorrido : " + idrecorrido );
         Recorrido recorrido = recService.getRecorrido(idrecorrido);
         Response<RecorridoDTO> resp;

        if (recorrido != null) {
            log.info("*** Recorridg15o recuperado " + recorrido.getDenominacion() );
            resp = new Response<RecorridoDTO>( false, 200,"Recorrido " + idrecorrido, new RecorridoDTO(recorrido));
        }
        else {
            log.info("*** No se encontro recorrido " + idrecorrido );
            resp = new Response<RecorridoDTO>( true, 300, "No se encontro recorrido " + idrecorrido, null );
        }
        return Mapper.getResponseAsJson(resp);
     }
     
}
