package com.unla.grupo16.models.dtos.responses;

// es la rta q se envia al cliente despues del login OK
public record LoginResponseDto(
        String token,
        String email,
        String rol, 
        Integer id,
        String nombreCompleto) {

}
