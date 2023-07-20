package com.stcu.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.stcu.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {

    public static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${stcu.app.jwtSecret}")
    private String jwtSecret;
    
    @Value("${stcu.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken( Authentication authentication ) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl)authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date( new Date().getTime()+jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token ){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken( String authToken ) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }
        catch(SignatureException ex ){ 
            logger.error("Invalid JWT Signature: {}", ex.getMessage());
        }
        catch( MalformedJwtException ex ){
            logger.error("Invalid JWT token: {}", ex.getMessage());
        }
        catch( ExpiredJwtException ex ){
            logger.error("JWT token is expired: {}", ex.getMessage());
        }
        catch( UnsupportedJwtException ex ){
            logger.error("JWT token is unnsoported: {}", ex.getMessage());
        }
        catch( IllegalArgumentException ex ){
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
}
