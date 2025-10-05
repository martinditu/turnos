package com.unla.grupo16.exception;

// CLIENTE
public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException() {
        super("Error negocio");
    }
}
