package com.unla.grupo16.configurations.security.jwt;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*pattern return: Manejador de acceso denegado (403 Forbidden) */
/* Se ejecuta cuando un usuario autenticado intenta acceder a un recurso sin los permisos necesarios */
/* Antes: El sistema podía desloguear al usuario */
/* Ahora: Devuelve un JSON con código 403 y mensaje claro para que el frontend muestre la página de error */
@Component
public class ErrorAccesoDenegado implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        /*pattern return: Construcción de respuesta JSON estructurada */
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        var errorBody = new java.util.HashMap<String, Object>();
        errorBody.put("timestamp", LocalDateTime.now().toString());
        errorBody.put("estado", 403);
        errorBody.put("error", "Acceso Denegado");
        errorBody.put("mensaje", "No tienes permisos para acceder a este recurso");
        errorBody.put("path", request.getRequestURI());
        
        /*pattern return: Serialización de respuesta a JSON */
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorBody));
    }
}
