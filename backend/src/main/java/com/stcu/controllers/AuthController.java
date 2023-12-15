package com.stcu.controllers;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.stcu.dto.request.LoginRequest;
import com.stcu.dto.request.SignupRequest;
import com.stcu.dto.response.JwtResponse;
import com.stcu.dto.response.MessageResponse;
import com.stcu.model.ERole;
import com.stcu.model.Rol;
import com.stcu.model.Usuario;
import com.stcu.repository.RolRepository;
import com.stcu.repository.UsuarioRepository;
import com.stcu.security.jwt.JwtUtils;
import com.stcu.security.services.UserDetailsImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    private static final Logger log = Logger.getLogger(AuthController.class.getName());

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser( @Valid @RequestBody LoginRequest loginRequest ) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword() )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map( item -> item.getAuthority())
            .collect( Collectors.toList() );
        
        log.info("*** Login user id: " + userDetails.getId() );
        return ResponseEntity.ok( 
            new JwtResponse( jwt, 
                            userDetails.getId(), 
                            userDetails.getUsername(), 
                            userDetails.getEmail(), 
                            roles ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (usuarioRepository.existsByUsuario(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Nombre de usuario ya esta en uso"));
        }
        // Crea nuevo usuario.
        Usuario usuario = new Usuario(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()));
        usuario.setEmail(signupRequest.getEmail());
        usuario.setApellido(signupRequest.getApellido());
        usuario.setNombre(signupRequest.getNombre());
        usuario.setDni(signupRequest.getDni());
        usuario.setDireccion(signupRequest.getDireccion());
        usuario.setTelefono(signupRequest.getTelefono());
        usuario.setAlta( Calendar.getInstance() );

        Set<String> strRoles = signupRequest.getRoles();
        Set<Rol> roles = new HashSet<>();
        if (strRoles == null) {
            Rol rol = rolRepository.findByRol(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: rol no encontrado"));
            roles.add(rol);
        } else {
            strRoles.forEach(rol -> {
                if (rol.compareTo("admin") == 0) {
                    Rol adminRol = rolRepository.findByRol(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: rol no encontrado"));
                    roles.add(adminRol);
                } else {
                    Rol userRol = rolRepository.findByRol(ERole.ROLE_USER)
                            .orElseThrow( () -> new RuntimeException("Error: rol no encontrado"));
                    roles.add(userRol);
                }
            });
        }
        usuario.setRoles(roles);
        usuarioRepository.save(usuario);
        log.info("*** Usuario registrado: " + usuario.getNombre() );
        return ResponseEntity.ok( new MessageResponse("Usuario registrado exitosamente"));
    }
    
}
