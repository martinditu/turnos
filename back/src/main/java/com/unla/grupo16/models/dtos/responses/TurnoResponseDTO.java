package com.unla.grupo16.models.dtos.responses;

public record TurnoResponseDTO(
        Integer id,
        String fecha,
        String hora,
        String nombreServicio,
        String ubicacionDescripcion,
        String nombreEmpleado,
        String nombreCliente,
        String codigoAnulacion
        ) {

}
