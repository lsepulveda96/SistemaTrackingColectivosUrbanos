package com.stcu.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stcu.controllers.dto.CoordenadaDTO;
import com.stcu.controllers.dto.ParadaDTO;

@RestController
@RequestMapping("/v1/mobile")
public class MobileController {
    
    @GetMapping("/test")
    ResponseEntity<?> testMobileApi() {
        ParadaDTO p = new ParadaDTO();
        p.setCodigo(100);
        p.setDescripcion("prueba parada respuesta");
        p.setDireccion("albarracin 145");
        p.setEstado("ACTIVA");
        p.setCoordenada( new CoordenadaDTO( -42.775935, -65.038144));
        
        return new ResponseEntity<>( p, HttpStatus.OK );
    }
}
