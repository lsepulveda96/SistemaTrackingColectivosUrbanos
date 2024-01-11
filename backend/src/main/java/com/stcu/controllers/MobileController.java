package com.stcu.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stcu.model.Linea;
import com.stcu.services.LineaServiceImp;

@RestController
@RequestMapping("/v1/mobile")
public class MobileController {

    private static final Logger log = Logger.getLogger(MobileController.class.getName());

    @Autowired
    LineaServiceImp serviceLinea;

    @GetMapping("/test")
    public String testMobileApi() {
        log.info("*** testMobileApi ");
        return "Conectado";
    }

    /**
     * Recupera todas las lineas activas disponibles
     * 
     * @return lista de lineas activas
     */
    @GetMapping("/lineas/activas")
    public String findAllLineas() {
        List<Linea> lineasActivas = serviceLinea.getLineasActivas();
        Response<List<Linea>> response = new Response<List<Linea>>(false, 200, "Lineas", lineasActivas);
        return Mapper.getResponseAsJson(response);
    }

}
