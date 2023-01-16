package com.stcu.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {
    
    private String token;
    private String type="Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse( String tkn, Long id, String username, String email, List<String> roles ) {
        this.token = tkn;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}