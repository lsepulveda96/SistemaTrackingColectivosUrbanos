package com.stcu.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Setter;
import lombok.Getter;

@Getter @Setter
public class UsuarioRequest {
    
    @Size(max=50)
    @Email
    private String email;

    private String nombre;

    private String apellido;

    private String dni;

    private String direccion;

    private String telefono; 

    private boolean superusuario;
}
