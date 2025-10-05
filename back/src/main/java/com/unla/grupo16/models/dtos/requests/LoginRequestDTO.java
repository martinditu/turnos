package com.unla.grupo16.models.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Schema(example = "usuario@mail.com")
        @Email(message = "Debe ser un email valido")
        @NotBlank(message = "El email no puede estar vacio")
        String email,
        @Schema(example = "password")
        @NotBlank(message = "La contraseña no puede estar vacia")
        String password
        ) {

}
