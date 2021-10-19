package com.stcu.controllers;

import java.util.List;

import com.stcu.model.Linea;
import com.stcu.services.LineaServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineaController {

    @Autowired
    LineaServiceImp service;

    @GetMapping("/lineas")
    public String findAllLineas() {
        List<Linea> lineas = service.getAllLineas();
        Response<List<Linea>> response = new Response<List<Linea>>( false,200,"Lineas", lineas );
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/linea/{id}")
    public String findLinea( @PathVariable long id ){
        Linea linea = service.getLinea( id );
        Response<Linea> response;
        if (linea != null) 
            response = new Response<Linea>( false,200,"Linea " + id, linea );
        else
            response = new Response<Linea>( true,400,"No se encontro linea " + id, null );
        return Mapper.getResponseAsJson(response);
    }    

    @PostMapping("/lineas")
    public String saveLinea( @RequestBody Linea linea ) {
        Response<Linea> response;
        try {
            Linea nlinea = service.saveLinea(linea);
            if (nlinea != null)
                response = new Response<Linea>( false,200,"Nueva linea registrada", nlinea );
            else    
                response = new Response<Linea>( true,400,"No se pudo registrar nueva Linea", null );
        }
        catch( org.hibernate.exception.ConstraintViolationException ex ) {
            System.err.println( "++++ Error guardando linea: "+ ex );
            response = new Response<Linea>( true,400,"Ya existe Linea con misma denominacion", null );
        }
        return Mapper.getResponseAsJson(response);
    }

    @PutMapping("/linea/{id}")
    public String updateLinea( @PathVariable long id, @RequestBody Linea linea ) {
        Linea ulinea = service.updateLinea(id, linea);
        Response<Linea> response;
        if (ulinea != null)
            response = new Response<Linea>( false, 200, "Linea " + ulinea.getDenominacion() + " actualizada", ulinea );
        else
            response = new Response<Linea>( true, 400, "No se pudo actualizar Linea " + id, null );
        return Mapper.getResponseAsJson(response);
    }
}
