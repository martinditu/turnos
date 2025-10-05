package com.unla.grupo16.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unla.grupo16.configurations.mapper.TurnoMapper;
import com.unla.grupo16.exception.NegocioException;
import com.unla.grupo16.exception.RecursoNoEncontradoException;
import com.unla.grupo16.models.dtos.responses.ClienteAdminDTO;
import com.unla.grupo16.models.dtos.responses.ClientesAdminResponseDTO;
import com.unla.grupo16.models.dtos.responses.TurnoResponseDTO;
import com.unla.grupo16.models.entities.Turno;
import com.unla.grupo16.services.interfaces.IClienteService;
import com.unla.grupo16.services.interfaces.ITurnoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "ADMIN", description = "Operaciones para la gestion de clientes y turnos (solo ADMIN)")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Validated // para editar mail
public class AdminController {

    private final IClienteService clienteService;
    private final ITurnoService turnoService;
    private final TurnoMapper turnoMapper;

    public AdminController(IClienteService clienteService, ITurnoService turnoService, TurnoMapper turnoMapper) {
        this.clienteService = clienteService;
        this.turnoService = turnoService;
        this.turnoMapper = turnoMapper;
    }

    @Operation(summary = "Listar turnos no disponibles (reservados)")
    @GetMapping("/turnos")
    public ResponseEntity<List<TurnoResponseDTO>> traerTurnosNoDisponibles() {
        List<Turno> turnos = turnoService.traerTurnosNoDisponibles();
        List<TurnoResponseDTO> dtos = turnos.stream()
                .map(turnoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Listar todos los clientes (activos y dados de baja)")
    @GetMapping("/clientes")
    public ResponseEntity<ClientesAdminResponseDTO> traerClientes() {
        return ResponseEntity.ok(clienteService.traerClientesActivosYBajaLogica());
    }

    @Operation(summary = "Dar de baja (soft delete) a un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente dado de baja correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto de negocio")
    })
    @PutMapping("/{clienteId}/baja")
    public ResponseEntity<String> darDeBajaCliente(
            @Parameter(description = "ID del cliente") @PathVariable Integer clienteId) {
        try {
            clienteService.darDeBajaCliente(clienteId);
            return ResponseEntity.ok("Cliente dado de baja correctamente.");
        } catch (NegocioException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado.");

        }
    }

    @Operation(summary = "Reactivar un cliente dado de baja")
    @PutMapping("/{clienteId}/alta")
    public ResponseEntity<String> darDeAltaCliente(
            @Parameter(description = "ID del cliente") @PathVariable Integer clienteId) {
        try {
            clienteService.darDeAltaCliente(clienteId);
            return ResponseEntity.ok("Cliente reactivado correctamente.");
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado.");
        }
    }

    @Operation(summary = "Editar informacion de un cliente")
    @PutMapping("/{clienteId}")
    public ResponseEntity<ClienteAdminDTO> editarCliente(
            @Parameter(description = "ID del cliente") @PathVariable Integer clienteId,
            @Valid @RequestBody ClienteAdminDTO clienteDto) {
        try {
            ClienteAdminDTO actualizado = clienteService.editarCliente(clienteId, clienteDto);
            return ResponseEntity.ok(actualizado);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
