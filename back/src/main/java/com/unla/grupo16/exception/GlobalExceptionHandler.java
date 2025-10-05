package com.unla.grupo16.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // AutenticacionException
    @ExceptionHandler(AutenticacionException.class)
    public ResponseEntity<Map<String, Object>> manejarAutenticacionException(AutenticacionException ex) {
        return construirResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    // UsuarioDeshabilitadoException
    @ExceptionHandler(UsuarioDeshabilitadoException.class)
    public ResponseEntity<Map<String, Object>> manejarUsuarioDeshabilitadoException(UsuarioDeshabilitadoException ex) {
        return construirResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // NegocioException 
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Map<String, Object>> manejarNegocioException(NegocioException ex) {
        return construirResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // RecursoNoEncontradoException
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return construirResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // DisponibilidadNoEncontradaException
    @ExceptionHandler(DisponibilidadNoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> manejarDisponibilidadNoEncontrada(DisponibilidadNoEncontradaException ex) {
        return construirResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // IllegalArgumentException 
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalArgument(IllegalArgumentException ex) {
        return construirResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // para cialquier excep no controlada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionGenerica(Exception ex) {
        return construirResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.");
    }

    // Método auxiliar para construir la respuesta de error en formato JSON
    private ResponseEntity<Map<String, Object>> construirResponse(HttpStatus status, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("estado", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        return new ResponseEntity<>(body, status);
    }

    // errores de validacion de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        Map<String, Object> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("estado", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("mensajes", errores); // lista de campos con errores

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
