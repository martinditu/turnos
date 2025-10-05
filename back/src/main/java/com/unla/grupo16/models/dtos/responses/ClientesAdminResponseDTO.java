package com.unla.grupo16.models.dtos.responses;

import java.util.List;

public record ClientesAdminResponseDTO(
        List<ClienteAdminDTO> clientesActivos,
        List<ClienteAdminDTO> clientesBajaLogica
        ) {

}
