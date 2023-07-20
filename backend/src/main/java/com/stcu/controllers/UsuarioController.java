package com.stcu.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.stcu.controllers.dto.UsuarioDTO;
import com.stcu.dto.request.UsuarioRequest;
import com.stcu.dto.response.MessageResponse;
import com.stcu.model.ERole;
import com.stcu.model.Rol;
import com.stcu.model.Usuario;
import com.stcu.repository.RolRepository;
import com.stcu.services.UsuarioServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

    @Autowired
    UsuarioServiceImp service;

    @Autowired
    RolRepository rolRepository;

    @GetMapping("/usuarios")
    public String getUsuarios() {
        List<Usuario> usrs = service.getAllUsuarios();

        Response<List<UsuarioDTO>> response = new Response<List<UsuarioDTO>>(false, 200, "Lista de usuarios",
                UsuarioDTO.toListUsuarioDTO(usrs));

        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/usuario/{id}")
    public String getUsuario(@PathVariable long id) {
        Usuario usr = service.getUsuario(id);
        Response<UsuarioDTO> response;

        if (usr != null) {
            usr.getRoles();
            response = new Response<UsuarioDTO>(false, 200, "Usuario " + id, new UsuarioDTO(usr));
        } else
            response = new Response<UsuarioDTO>(true, 400, "No se pudo encontrar usuario " + id, null);

        return Mapper.getResponseAsJson(response);
    }


    @PutMapping("/usuario/{id}")
    public String updateUsuario(@PathVariable long id, @Valid @RequestBody UsuarioRequest usrReq) {
        Usuario usrUpd = new Usuario();
        usrUpd.setApellido(usrReq.getApellido());
        usrUpd.setNombre(usrReq.getNombre());
        usrUpd.setDni(usrReq.getDni());
        usrUpd.setDireccion(usrReq.getDireccion());
        usrUpd.setTelefono(usrReq.getTelefono());
        usrUpd.setEmail(usrReq.getEmail());
        Set<Rol> roles = new HashSet<>();
        Rol userRol = rolRepository.findByRol(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: rol no encontrado"));
        roles.add(userRol);
        if (usrReq.isSuperusuario() == true) {
            Rol admRol = rolRepository.findByRol(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: rol no encontrado"));
            roles.add(admRol);
        }
        usrUpd.setRoles(roles);
        Usuario usuario = service.updateUsuario(id, usrUpd);

        Response<UsuarioDTO> response;

        if (usuario != null)
            response = new Response<UsuarioDTO>(false, 200, "Usuario " + id + " actualizado", new UsuarioDTO(usuario));
        else
            response = new Response<UsuarioDTO>(true, 400, "No se pudo actualizar usuario " + id, null);

        return Mapper.getResponseAsJson(response);
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<?> deactivateUsuario( @PathVariable long id ) {
        boolean stat = this.service.deactivateUsuario(id);
        if (stat)
            return ResponseEntity.ok( new MessageResponse("Usuario desactivado"));
        else
        return ResponseEntity.badRequest()
                             .body(new MessageResponse("No se pudo desactivar usuario"));
    }

    @GetMapping("/usuario/activate/{id}")
    public ResponseEntity<?> activateUsuario( @PathVariable long id ){
        boolean stat = this.service.activateUsuario(id);
        if (stat)
            return ResponseEntity.ok( new MessageResponse("Usuario activado"));
        else
        return ResponseEntity.badRequest()
                             .body(new MessageResponse("No se pudo activar usuario"));
    }
}
