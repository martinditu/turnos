package com.unla.grupo16.configurations.security.jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unla.grupo16.services.impl.UserServiceImp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// extiende OncePerRequestFilter : garantiza una sola ejecución por request
@Component
public class FiltroAutenticacion extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserServiceImp userDetailsService;

    public FiltroAutenticacion(JwtUtil jwtUtil, UserServiceImp userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String tokenJwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenJwt = authHeader.substring(7);
            try {
                username = jwtUtil.traerUsernameDesdeToken(tokenJwt);
            } catch (Exception e) {
                setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token invalido");
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validarToken(tokenJwt, userDetails)) {
                List<String> roles;
                try {
                    roles = jwtUtil.traerRolesDesdeToken(tokenJwt);
                } catch (Exception e) {
                    roles = null;
                }

                List<GrantedAuthority> authorities = (roles != null)
                        ? roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                        : new ArrayList<>(userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token expirado o invalido");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String mensaje)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        Map<String, Object> body = Map.of(
                "fechaHora", LocalDateTime.now().toString(),
                "estado", status.value(),
                "error", status.getReasonPhrase(),
                "mensaje", mensaje
        );

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
