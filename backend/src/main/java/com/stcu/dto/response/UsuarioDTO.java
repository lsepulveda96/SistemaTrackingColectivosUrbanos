package com.stcu.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.stcu.model.ERole;
import com.stcu.model.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {

    private long id;

    private String usuario;

    private String nombre;

    private String apellido;

    private String dni;

    private String direccion;

    private String telefono;

    private String email;

    private boolean superusuario;

    private String estado;

    private List<ERole> roles;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Usuario usr) {
        id = usr.getId();
        usuario = usr.getUsuario();
        nombre = usr.getNombre();
        apellido = usr.getApellido();
        dni = usr.getDni();
        direccion = usr.getDireccion();
        telefono = usr.getTelefono();
        email = usr.getEmail();
        superusuario = usr.isSuperusuario();
        estado = usr.getEstado();
        roles = usr.getRoles().stream().map(item -> item.getRol()).collect(Collectors.toList());
    }

    public static List<UsuarioDTO> toListUsuarioDTO(List<Usuario> listUsuarios) {
        List<UsuarioDTO> list = new ArrayList<UsuarioDTO>();
        listUsuarios.forEach(usr -> {
            list.add(toDTO(usr));
        });
        return list;
    }

    private static UsuarioDTO toDTO(Usuario usr) {
        UsuarioDTO usrDto = new UsuarioDTO();
        usrDto.setId(usr.getId());
        usrDto.setUsuario(usr.getUsuario());
        usrDto.setNombre(usr.getNombre());
        usrDto.setApellido(usr.getApellido());
        usrDto.setDni(usr.getDni());
        usrDto.setDireccion(usr.getDireccion());
        usrDto.setTelefono(usr.getTelefono());
        usrDto.setEmail(usr.getEmail());
        usrDto.setSuperusuario(usr.isSuperusuario());
        usrDto.setEstado(usr.getEstado());

        return usrDto;
    }

    @Override
    public String toString() {
        return "UsuarioDTO [apellido=" + apellido + ", direccion=" + direccion + ", dni=" + dni + ", email=" + email
                + ", estado=" + estado + ", id=" + id + ", nombre=" + nombre + ", superusuario=" + superusuario
                + ", telefono=" + telefono + ", usuario=" + usuario + "]";
    }

}
