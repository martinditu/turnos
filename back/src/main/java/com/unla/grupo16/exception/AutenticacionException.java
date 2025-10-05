package com.unla.grupo16.exception;

// AUTH
public class AutenticacionException extends NegocioException {

    public AutenticacionException() {
        super("Credenciales inválidas");
    }

    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
}
