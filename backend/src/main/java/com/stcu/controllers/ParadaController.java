package com.stcu.controllers;

import java.util.List;

import com.stcu.controllers.dto.ParadaDTO;
import com.stcu.model.Parada;
import com.stcu.services.ParadaServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class ParadaController {
    
    @Autowired
    ParadaServiceImp service;

    @GetMapping("/paradas")
    public String findAllParadas() {
        List<Parada> paradas = service.getAllParadas();
        
        Response<List<ParadaDTO>> response = new Response<List<ParadaDTO>>( false,200,"Listado de paradas", ParadaDTO.toListParadaDTO(paradas) );
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/paradas/activas")
    public String findAllParadasActivas() {
        List<Parada> paradasActivas = service.getAllParadasActivas();
        
        Response<List<ParadaDTO>> response = new Response<List<ParadaDTO>>( false,200,"Listado de paradas", ParadaDTO.toListParadaDTO(paradasActivas) );
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/parada/{codigo}")
    public String findParada( @PathVariable  long codigo ) {
        Parada parada = service.getParada(codigo);
        Response<ParadaDTO> response;
        if (parada != null )
            response = new Response<ParadaDTO>( false,200,"Parada " + codigo, new ParadaDTO(parada) );
        else
            response = new Response<ParadaDTO>( true, 400,"No se pudo recuperar Parada " + codigo, null );
        return Mapper.getResponseAsJson(response);
    }

    @PostMapping("/paradas")
    public String saveParada( @RequestBody ParadaDTO nParada ) {        
        Parada newParada = service.saveParada( nParada.ToParada() );
        Response<ParadaDTO> response;
        if (newParada != null)
            response = new Response<ParadaDTO>( false, 200,"Nueva parada creada", new ParadaDTO(newParada) );
        else
            response = new Response<ParadaDTO>( true, 400,"No se pudo guardar nueva parada", null );
        return Mapper.getResponseAsJson(response);
    }

    @PutMapping("/parada/{codigo}")
    public String updateParada( @PathVariable long codigo, @RequestBody ParadaDTO paradaDto ) {
        Parada parada = service.updateParada(codigo, paradaDto.ToParada() );
        Response<ParadaDTO> response;
        if (parada != null)
            response = new Response<ParadaDTO>( false, 200,"Parada " + codigo + " actualizada", new ParadaDTO(parada) );
        else    
            response = new Response<ParadaDTO>( true, 400, "No se puedo actualizar parada " + codigo, null );
        return Mapper.getResponseAsJson(response);
    }

    @DeleteMapping("/parada/activardesactivar/{disable}/{codigo}")
    public String disableParada( @PathVariable boolean disable, @PathVariable long codigo ) {
        Parada parada = service.enableDisableParada(codigo, disable);
        Response<ParadaDTO> response;
        if (parada != null) {
            String msg = disable ? ("Parada " + codigo + " deshabilitada"): ("Parada " + codigo + " habilitada");
            response = new Response<ParadaDTO>( false, 200, msg, null );
        }
        else {
            String msg = disable ? ("No se pudo deshabilitar Parada " + codigo): ("No se pudo habilitar Parada " + codigo);
            response = new Response<ParadaDTO>( true, 400, msg, null );
        }
        return Mapper.getResponseAsJson(response);
    }
}
