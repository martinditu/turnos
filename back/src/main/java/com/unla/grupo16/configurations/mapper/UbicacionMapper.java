package com.unla.grupo16.configurations.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.unla.grupo16.models.dtos.responses.UbicacionResponseDTO;
import com.unla.grupo16.models.entities.Ubicacion;

@Component
public class UbicacionMapper {

    public UbicacionResponseDTO toDTO(Ubicacion ubicacion) {
        return new UbicacionResponseDTO(
            ubicacion.getId(),
            ubicacion.getDireccion(),
            ubicacion.getLocalidad() != null
                ? ubicacion.getLocalidad().getNombre()
                : "Sin localidad"
        );
    }

    public List<UbicacionResponseDTO> toDTOList(Set<Ubicacion> ubicaciones) {
        return ubicaciones.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

// OK