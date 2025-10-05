package com.unla.grupo16.models.dtos.responses;

import java.sql.Timestamp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteAdminDTO(
        Integer id,
        @NotBlank(message = "El nombre no puede estar vacio")
        @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
        String nombre,
        @NotBlank(message = "El apellido no puede estar vacio")
        @Size(max = 100, message = "El apellido no debe superar los 100 caracteres")
        String apellido,
        @NotBlank(message = "El DNI no puede estar vacio")
        @Size(min = 7, max = 10, message = "El DNI debe tener entre 7 y 10 dígitos")
        String dni,
        @NotBlank(message = "El email no puede estar vacio")
        @Email(message = "Debe ser un email válido")
        String email,
        Timestamp createdAt,
        Timestamp updatedAt,
        boolean clienteActivo,
        boolean tieneTurnosActivos
        ) {

}
