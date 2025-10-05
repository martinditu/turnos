package com.unla.grupo16.exception;

// CLIENTE
public class DisponibilidadNoEncontradaException extends RuntimeException {

    public DisponibilidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }

    public DisponibilidadNoEncontradaException() {
        super("Error disponibilidad");
    }

}
