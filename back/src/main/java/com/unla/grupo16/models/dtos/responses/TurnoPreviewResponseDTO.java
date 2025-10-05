package com.unla.grupo16.models.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;

public record TurnoPreviewResponseDTO(
        ServicioResponseDTO servicio,
        UbicacionResponseDTO ubicacion,
        LocalDate fecha,
        LocalTime hora
        ) {

}
