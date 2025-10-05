package com.unla.grupo16.services.interfaces;

import com.unla.grupo16.exception.NegocioException;
import com.unla.grupo16.exception.RecursoNoEncontradoException;
import com.unla.grupo16.models.dtos.responses.ClienteAdminDTO;
import com.unla.grupo16.models.dtos.responses.ClientesAdminResponseDTO;

public interface IClienteService {

    // OK
    // ADMIN
    ClientesAdminResponseDTO traerClientesActivosYBajaLogica();

    void darDeBajaCliente(Integer clienteId) throws NegocioException, RecursoNoEncontradoException;

    void darDeAltaCliente(Integer clienteId) throws RecursoNoEncontradoException;

    ClienteAdminDTO editarCliente(Integer clienteId, ClienteAdminDTO clienteDto) throws RecursoNoEncontradoException;

}
