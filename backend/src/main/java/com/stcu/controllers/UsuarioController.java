package com.stcu.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.stcu.controllers.dto.UsuarioDTO;
import com.stcu.dto.request.UsuarioRequest;
import com.stcu.dto.request.PassRequest;
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
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    UsuarioServiceImp service;

    @Autowired
    RolRepository rolRepository;

    private static final Logger log = Logger.getLogger(UsuarioController.class.getName());

    @GetMapping("/usuarios")
    public String getUsuarios() {
        log.info("*** getUsuarios");
        List<Usuario> usrs = service.getAllUsuarios();

        log.info("*** Lista de usuarios length " + usrs.size());
        Response<List<UsuarioDTO>> response = new Response<List<UsuarioDTO>>(false, 200, "Lista de usuarios",
                UsuarioDTO.toListUsuarioDTO(usrs));

        return Mapper.getResponseAsJson(response);
    }

    @GetMapping("/usuario/{id}")
    public String getUsuario(@PathVariable long id) {
        log.info("*** getUsuario : " + id);
        Usuario usr = service.getUsuario(id);
        Response<UsuarioDTO> response;

        if (usr != null) {
            log.info("*** Usuario " + id + " recuperado: " + usr.getNombre());
            response = new Response<UsuarioDTO>(false, 200, "Usuario " + id, new UsuarioDTO(usr));
        } else {
            log.info("*** No se pudo recuperar usuario " + id);
            response = new Response<UsuarioDTO>(true, 400, "No se pudo encontrar usuario " + id, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    @PutMapping("/usuario/{id}")
    public String updateUsuario(@PathVariable long id, @Valid @RequestBody UsuarioRequest usrReq) {
        log.info("*** updateUsuario : " + id);
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

        if (usuario != null) {
            log.info("*** Usuario " + id + " actualizado");
            response = new Response<UsuarioDTO>(false, 200, "Usuario " + id + " actualizado", new UsuarioDTO(usuario));
        } else {
            log.info("*** No se pudo actualizar Usuario " + id);
            response = new Response<UsuarioDTO>(true, 400, "No se pudo actualizar usuario " + id, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<?> deactivateUsuario(@PathVariable long id) {
        log.info("*** deactivateUsuario : " + id);
        boolean stat = this.service.deactivateUsuario(id);
        if (stat) {
            log.info("*** Usuario " + id + " desactivado");
            return ResponseEntity.ok(new MessageResponse("Usuario desactivado"));
        } else {
            log.info("*** No se pudo desactivar usuario " + id);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("No se pudo desactivar usuario"));
        }
    }

    @GetMapping("/usuario/activate/{id}")
    public ResponseEntity<?> activateUsuario(@PathVariable long id) {
        log.info("*** activateUsuario : " + id);
        boolean stat = this.service.activateUsuario(id);
        if (stat) {
            log.info("*** Usuario " + id + " activado");
            return ResponseEntity.ok(new MessageResponse("Usuario activado"));
        } else {
            log.info("*** No se pudo activar usuario " + id);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("No se pudo activar usuario"));
        }
    }

    @PutMapping("/usuario/changepass/{id}")
    public ResponseEntity<?> changePasswd(@PathVariable long id,
            @Valid @RequestBody PassRequest pass) {
        log.info("*** Cambiar password usuario " + id);
        boolean status = this.service.changePass(id, pass.getActualPass(), pass.getNewPass());
        if (status) {
            log.info("*** Password actualizado Usuario " + id);
            return ResponseEntity.ok(new MessageResponse("passwod cambiado"));
        } else {
            log.info("*** No se pudo actualizar password usuario " + id);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("No se pudo cambiar password usuario"));
        }

    }
}
