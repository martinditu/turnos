package com.unla.grupo16.configurations.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // clave para poder firmar el token 
    @Value("${jwt.claveSecreta}")
    private String claveSecreta;

    // tiempo de expiracion del token 
    @Value("${jwt.tiempoExpiracion}")
    private long tiempoExpiracion;

    // crea key segura basada en HS512
    private Key crearClaveFirma() {
        return Keys.hmacShaKeyFor(claveSecreta.getBytes(StandardCharsets.UTF_8));
    }

    // crea un token jwt con los roles del usuario
    public String generarToken(UserDetails userDetails) {
        Map<String, Object> datosToken = new HashMap<>();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        datosToken.put("roles", roles);

        return Jwts.builder()
                .setClaims(datosToken)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tiempoExpiracion))
                .signWith(crearClaveFirma(), SignatureAlgorithm.HS512)
                .compact();
    }

    // valida q el token pertenezca al usuario y no haya expirado
    public boolean validarToken(String token, UserDetails userDetails) {
        try {
            final String username = traerUsernameDesdeToken(token);
            return username.equals(userDetails.getUsername()) && !tokenExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }

    // metodos para extraer datos del token 
    public String traerUsernameDesdeToken(String token) {
        return traerDatosDesdeToken(token, Claims::getSubject);
    }

    public Date traerFechaExpiracionDesdeToken(String token) {
        return traerDatosDesdeToken(token, Claims::getExpiration);
    }

    // extraer roles del token
    public <T> T traerDatosDesdeToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = traerTodosLosDatosDesdeToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims traerTodosLosDatosDesdeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(crearClaveFirma())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean tokenExpirado(String token) {
        return traerFechaExpiracionDesdeToken(token).before(new Date());
    }

    // extraer roles del token
    public List<String> traerRolesDesdeToken(String token) {
        Claims claims = traerTodosLosDatosDesdeToken(token);
        List<?> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

}
