package com.unla.grupo16.configurations.mapper;

import org.springframework.stereotype.Component;

import com.unla.grupo16.models.dtos.responses.ClienteAdminDTO;
import com.unla.grupo16.models.entities.Cliente;
import com.unla.grupo16.models.entities.UserEntity;

@Component
public class ClienteMapper {

    public ClienteAdminDTO toDTO(UserEntity user) {
        if (!(user.getPersona() instanceof Cliente cliente)) {
            throw new IllegalArgumentException("La persona asociada no es un Cliente");
        }

        boolean tieneTurnosActivos = cliente.getTurnos().stream()
                .anyMatch(turno -> !turno.isDisponible());

        return new ClienteAdminDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getDni(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isActivo(),
                tieneTurnosActivos
        );

    }
}
