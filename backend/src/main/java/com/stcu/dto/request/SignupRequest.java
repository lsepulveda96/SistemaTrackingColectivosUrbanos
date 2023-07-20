package com.stcu.dto.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequest {
    
    @NotBlank
    @Size(min=3, max=20)
    private String username;

    @NotBlank
    @Size(min=6, max=40)
    private String password;

    private Set<String> roles;

    @Size(max=50)
    @Email
    private String email;

    private String nombre;

    private String apellido;

    private String dni;

    private String direccion;

    private String telefono; 
}
