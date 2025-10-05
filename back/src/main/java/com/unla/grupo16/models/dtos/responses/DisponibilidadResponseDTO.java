package com.unla.grupo16.models.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DisponibilidadResponseDTO(
        LocalDate fecha,
        boolean activo,
        List<HorarioDTO> horarios
        ) {

    public record HorarioDTO(
            LocalTime horaInicio,
            LocalTime horaFin
            ) {

    }
}
