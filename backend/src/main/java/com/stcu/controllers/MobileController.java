package com.stcu.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stcu.controllers.dto.CoordenadaDTO;
import com.stcu.controllers.dto.ParadaDTO;

@RestController
@RequestMapping("/api/mobile")
public class MobileController {
    
    @GetMapping("/test")
    public String testMobileApi() {

        
        return "Conectado";
    }
}
