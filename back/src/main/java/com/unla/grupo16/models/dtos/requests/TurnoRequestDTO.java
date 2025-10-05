package com.unla.grupo16.models.dtos.requests;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TurnoRequestDTO(
        @Schema(example = "1")
        @NotNull(message = "El ID del servicio es obligatorio")
        Integer idServicio,
        @Schema(example = "1")
        @NotNull(message = "El ID de la ubicacion es obligatorio")
        Integer idUbicacion,
        @Schema(example = "2025-06-01")
        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,
        @Schema(example = "10:00")
        @NotNull(message = "La hora es obligatoria")
        LocalTime hora
        ) {

}
