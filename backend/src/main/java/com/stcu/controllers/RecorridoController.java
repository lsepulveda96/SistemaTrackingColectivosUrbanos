package com.stcu.controllers;

import java.util.List;
import java.util.logging.Logger;

import com.stcu.controllers.dto.ParadaRecorridoDTO;
import com.stcu.controllers.dto.RecorridoDTO;
import com.stcu.model.ParadaRecorrido;
import com.stcu.model.Recorrido;
import com.stcu.services.RecorridoServiceImp;

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
public class RecorridoController {

    @Autowired
    RecorridoServiceImp service;

    private static final Logger log = Logger.getLogger(RecorridoController.class.getName());

    @GetMapping("/recorridos/{idlinea}")
    public String findAllRecorridos(@PathVariable long idlinea) {
        log.info("*** findAllRecorridos, linea : " + idlinea);
        List<Recorrido> recorridos = service.getRecorridosLinea(idlinea);
        log.info("*** Lista de Recorridos linea " + idlinea + ", length: " + recorridos.size());
        Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>(false, 200,
                "Recorridos de linea " + idlinea, RecorridoDTO.toListRecorridosDTO(recorridos));
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/recorridos/activo/{idlinea}")
    public String findRecorridosActivos(@PathVariable long idlinea) {
        log.info("*** findRecorridosActivos, linea : " + idlinea);
        List<Recorrido> recorridos = service.getRecorridosActivos(idlinea);
        log.info("*** Lista de Recorridos activos linea " + idlinea + ", length: " + recorridos.size());

        Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>(false, 200,
                "Recorridos activos de linea " + idlinea, RecorridoDTO.toListRecorridosDTO(recorridos));
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/recorridos/noactivo/{idlinea}")
    public String findRecorridosNoActivos(@PathVariable long idlinea) {
        log.info("*** findRecorridosNoActivos, linea : " + idlinea);
        List<Recorrido> recorridos = service.getRecorridosActivos(idlinea);

        log.info("*** Lista de Recorridos no activos linea " + idlinea + ", length: " + recorridos.size());
        Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>(false, 200,
                "Recorridos activos de linea " + idlinea, RecorridoDTO.toListRecorridosDTO(recorridos));
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/recorrido/paradas/{idrecorrido}")
    public String findParadasRecorrido(@PathVariable long idrecorrido) {
        log.info("*** findParadasRecorrido, recorrido : " + idrecorrido);
        List<ParadaRecorrido> paradas = service.getParadasRecorrido(idrecorrido);

        Response<List<ParadaRecorridoDTO>> response;
        if (paradas != null) {
            log.info("*** Lista de paradas de recorrido " + idrecorrido + ", length: " + paradas.size());
            response = new Response<List<ParadaRecorridoDTO>>(false, 200, "Paradas de recorrido " + idrecorrido,
                    ParadaRecorridoDTO.toListParadaRecorridoDTO(paradas));
        } else {
            log.info("*** No se pudo recuperar Lista de paradas de recorrido " + idrecorrido);
            response = new Response<List<ParadaRecorridoDTO>>(true, 400,
                    "No se pudo recuperar lista de paradas de recorrido " + idrecorrido, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/recorrido/{id}")
    public String findRecorrido(@PathVariable long id) {
        log.info("*** findRecorrido : " + id);
        Recorrido recorrido = service.getRecorrido(id);
        Response<RecorridoDTO> response;
        if (recorrido != null) {
            log.info("*** Recorrido: " + recorrido.getDenominacion());
            response = new Response<RecorridoDTO>(false, 200, "Recorrido " + id, new RecorridoDTO(recorrido));
        } else {
            log.info("*** No se encontro Recorrido: " + id);
            response = new Response<RecorridoDTO>(true, 400, "No se encontro recorrido " + id, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    @PostMapping("/recorridos")
    public String saveRecorrido(@RequestBody RecorridoDTO drec) {
        log.info("*** saveRecorrido : " + drec.getDenominacion() + ", linea: " + drec.getLinea().getDenominacion());
        Response<RecorridoDTO> response;

        if (service.existDenominacion(drec.getLinea().getId(), drec.getDenominacion()) != 0) {
            log.info("*** Nuevo Recorrido no registrado, denominacion duplicada para una misma linea");
            response = new Response<RecorridoDTO>(true, 400,
                    "Ya existe recorrido activo con igual denominacion, ingrese otro valor en el campo denominacion",
                    null);
        } else {
            Recorrido newRec = service.saveRecorrido(drec.toRecorrido());
            if (newRec != null) {
                log.info("*** Nuevo recorrido registrado");
                response = new Response<RecorridoDTO>(false, 200, "Nuevo Recorrido creado", new RecorridoDTO(newRec));
            } else {
                log.info("*** No se pudo registrar nuevo recorrido");
                response = new Response<RecorridoDTO>(true, 400, "No se pudo guardar nuevo Recorrido", null);
            }
        }
        return Mapper.getResponseAsJson(response);
    }

    @PutMapping("/recorrido/{idrec}")
    public String updateRecorrido(@PathVariable long idrec, @RequestBody RecorridoDTO drec) {
        log.info("*** updateRecorrido : " + idrec);
        Response<RecorridoDTO> response;
        long idRecDenom = service.existDenominacion(drec.getLinea().getId(), drec.getDenominacion());
        if (idRecDenom != 0 && idRecDenom != idrec) {
            log.info("*** Recorrido no actualizado, denominacion duplicada para una misma linea");
            response = new Response<RecorridoDTO>(true, 400,
                    "Ya existe recorrido activo con denominacion " + drec.getDenominacion()
                            + ", ingrese otra denominacion",
                    null);
        } else {
            Recorrido updRec = service.updateRecorrido(idrec, drec.toRecorrido());
            if (updRec != null) {
                log.info("*** Recorrido " + idrec + " actualizado");
                response = new Response<RecorridoDTO>(false, 200,
                        "Recorrido " + updRec.getDenominacion() + " actualizado", new RecorridoDTO(updRec));
            } else {
                log.info("*** No se pudo actualizar Recorrido " + idrec);
                response = new Response<RecorridoDTO>(true, 400,
                        "No se pudo actualizar recorrido " + drec.getDenominacion(), null);
            }
        }
        return Mapper.getResponseAsJson(response);
    }

    @DeleteMapping("/recorrido/{idrec}")
    public String deactivateRecorrido(@PathVariable long idrec) {
        log.info("*** deactivateRecorrido : " + idrec);
        Response<RecorridoDTO> response;
        Recorrido rec = service.deactivateRecorrido(idrec);
        if (rec != null) {
            log.info("*** Recorrido " + idrec + " desactivado");
            response = new Response<RecorridoDTO>(false, 200, "Recorrido " + idrec + " desactivado ",
                    new RecorridoDTO(rec));
        }
        else {
            log.info("*** No se pudo desactivar Recorrido " + idrec);
            response = new Response<RecorridoDTO>(true, 400, "No se pudo desctivar recorrido", null);
        }
        return Mapper.getResponseAsJson(response);
    }

}
