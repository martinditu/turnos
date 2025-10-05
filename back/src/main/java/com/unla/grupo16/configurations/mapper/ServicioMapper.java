package com.unla.grupo16.configurations.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.unla.grupo16.models.dtos.responses.ServicioResponseDTO;
import com.unla.grupo16.models.entities.Servicio;

@Component
public class ServicioMapper {

    public ServicioResponseDTO toDTO(Servicio servicio) {
        return new ServicioResponseDTO(
            servicio.getId(),
            servicio.getNombre(),
            servicio.getDescripcion(),
            servicio.getDuracion()
        );
    }

    public List<ServicioResponseDTO> toDTOList(List<Servicio> servicios) {
        return servicios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
