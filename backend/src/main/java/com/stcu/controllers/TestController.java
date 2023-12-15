package com.stcu.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    private static final Logger log = Logger.getLogger(TestController.class.getName());

    @GetMapping("/hola")
    public String hola() {
        log.info("*** hola ");
        return "HOLA SPRING BOOT 2.7 ON TOMCAT9";
    }
}
