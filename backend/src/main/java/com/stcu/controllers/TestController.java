package com.stcu.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/hola")
    public String hola() {
        return "HOLA SPRING BOOT 2.7 ON TOMCAT9";
    }
}
