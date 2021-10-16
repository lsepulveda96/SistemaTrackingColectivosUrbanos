package com.stcu.controllers;

import java.util.List;

import com.stcu.model.Colectivo;
import com.stcu.services.ColectivoServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ColectivoController {

    @Autowired
    ColectivoServiceImp service;
    
    @GetMapping("/colectivos")
    public String findAllColectivos() {
        List<Colectivo> list = service.getAllColectivos();
        Response<List<Colectivo>> response = new Response<List<Colectivo>>( false, 200, "Listado de colectivos", list );
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/colectivo/{id}")
    public String findColectivo( @PathVariable long id ) {
        Colectivo col = service.getColectivo(id);
        Response<Colectivo> response;
        if (col != null) 
            response = new Response<Colectivo>( false,200, "Colectivo id " + id, col );
        else    
            response = new Response<Colectivo>( true, 400, "No se encontro colectivo id = " + id, null );
        return Mapper.getResponseAsJson(response);
    }

    @PostMapping("/colectivos")
    public String saveColectivo( @RequestBody Colectivo col ) {
        System.out.println("****** Save Colectivo: " + col.toString() );
        
        Colectivo colectivo = service.saveColectivo(col);
        Response<Colectivo> response;
        if (colectivo != null) 
            response = new Response<Colectivo> (false, 200, "Nuevo Colectivo registrado", colectivo );
        else 
            response = new Response<Colectivo> (true, 400, "No se pudo registrar nuevo colectivo", null );
    
        return Mapper.getResponseAsJson(response);
    }

    @PutMapping("/colectivo/{id}")
    public String updateColectivo( @PathVariable long id, @RequestBody Colectivo col ) {
        Colectivo colectivo = service.updateColectivo(id, col);
        Response<Colectivo> response;
        if (colectivo != null)
            response = new Response<Colectivo>(false, 200, "Colectivo " + id + " actualizado", colectivo );
        else 
            response = new Response<Colectivo>(true, 400, "No se pudo actualizar colectivo " + id, null );
        return Mapper.getResponseAsJson(response);
    }
}
