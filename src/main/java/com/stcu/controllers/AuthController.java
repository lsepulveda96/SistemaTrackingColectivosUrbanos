package com.stcu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stcu.model.Usuario;
import com.stcu.repository.UsuarioRepository;
import com.stcu.utils.JWTUtil;


@RestController
public class AuthController {
    /* 
    @Autowired(required = true)
    private UsuarioRepository usuarioRepository;

    @Autowired(required =true)
    private JWTUtil jwtUtil; */

    @PostMapping("/login")
    public String login( @RequestBody Usuario usuario ) {
        return "";
    }
}
