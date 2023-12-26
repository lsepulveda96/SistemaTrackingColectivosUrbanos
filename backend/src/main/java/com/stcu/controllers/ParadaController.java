package com.stcu.controllers;

import java.util.List;
import java.util.logging.Logger;

import com.stcu.dto.response.ParadaDTO;
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

    private static final Logger log = Logger.getLogger(ParadaController.class.getName());

    /**
     * Recupera listado completo de paradas.
     * 
     * @return
     */
    @GetMapping("/paradas")
    public String findAllParadas() {
        log.info("*** findAllParadas");
        List<Parada> paradas = service.getAllParadas();
        log.info("*** Listado de Paradas " + paradas.size());
        Response<List<ParadaDTO>> response = new Response<List<ParadaDTO>>(false, 200, "Listado de paradas",
                ParadaDTO.toListParadaDTO(paradas));
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Busca y retorna listado de paradas activas
     * 
     * @return
     */
    @GetMapping("/paradas/activas")
    public String findAllParadasActivas() {
        log.info("*** findAllParadasActivas");
        List<Parada> paradasActivas = service.getAllParadasActivas();
        log.info("*** Listado de paradas activoas " + paradasActivas.size());
        Response<List<ParadaDTO>> response = new Response<List<ParadaDTO>>(false, 200, "Listado de paradas",
                ParadaDTO.toListParadaDTO(paradasActivas));
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Busca y retorna un parada a partir de su id.
     * 
     * @param codigo
     * @return
     */
    @GetMapping("/parada/{codigo}")
    public String findParada(@PathVariable long codigo) {
        log.info("*** findParada : " + codigo);
        Parada parada = service.getParada(codigo);
        Response<ParadaDTO> response;
        if (parada != null) {
            log.info("*** Parada recuperada " + parada.getDireccion());
            response = new Response<ParadaDTO>(false, 200, "Parada " + codigo, new ParadaDTO(parada));
        } else {
            log.info("*** No se pudo recuperar parada con codigo " + codigo);
            response = new Response<ParadaDTO>(true, 400, "No se pudo recuperar Parada " + codigo, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Registra una nueva parada.
     * 
     * @param nParada
     * @return
     */
    @PostMapping("/paradas")
    public String saveParada(@RequestBody ParadaDTO nParada) {
        log.info("*** saveParada : " + nParada.getDireccion());
        Parada newParada = service.saveParada(nParada.ToParada());
        Response<ParadaDTO> response;
        if (newParada != null) {
            log.info("*** Nueva parada registrada " + newParada.getCodigo());
            response = new Response<ParadaDTO>(false, 200, "Nueva parada creada", new ParadaDTO(newParada));
        } else {
            log.info("*** No se pudo registrar nueva parada ");
            response = new Response<ParadaDTO>(true, 400, "No se pudo guardar nueva parada", null);
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Actualiza los datos de una parada.
     * 
     * @param codigo
     * @param paradaDto
     * @return
     */
    @PutMapping("/parada/{codigo}")
    public String updateParada(@PathVariable long codigo, @RequestBody ParadaDTO paradaDto) {
        log.info("*** updateParada : " + codigo);
        Parada parada = service.updateParada(codigo, paradaDto.ToParada());
        Response<ParadaDTO> response;
        if (parada != null) {
            log.info("*** Parada " + codigo + " actualizada");
            response = new Response<ParadaDTO>(false, 200, "Parada " + codigo + " actualizada", new ParadaDTO(parada));
        } else {
            log.info("*** No se pudo actualizar parada " + codigo);
            response = new Response<ParadaDTO>(true, 400, "No se puedo actualizar parada " + codigo, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Habilita o deshabilita una parada.
     * 
     * @param disable
     * @param codigo
     * @return
     */
    @DeleteMapping("/parada/activardesactivar/{disable}/{codigo}")
    public String disableParada(@PathVariable boolean disable, @PathVariable long codigo) {
        log.info("*** disableParada : " + codigo + ", " + disable);
        Parada parada = service.enableDisableParada(codigo, disable);
        Response<ParadaDTO> response;
        if (parada != null) {
            String msg = disable ? ("Parada " + codigo + " deshabilitada") : ("Parada " + codigo + " habilitada");
            log.info("*** " + msg);
            response = new Response<ParadaDTO>(false, 200, msg, null);
        } else {
            String msg = disable ? ("No se pudo deshabilitar Parada " + codigo)
                    : ("No se pudo habilitar Parada " + codigo);
            log.info("*** " + msg);
            response = new Response<ParadaDTO>(true, 400, msg, null);
        }
        return Mapper.getResponseAsJson(response);
    }
}
