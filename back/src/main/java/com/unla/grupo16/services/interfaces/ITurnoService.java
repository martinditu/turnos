package com.unla.grupo16.services.interfaces;

import java.time.LocalDate;
import java.util.List;

import com.unla.grupo16.exception.NegocioException;
import com.unla.grupo16.models.dtos.requests.TurnoRequestDTO;
import com.unla.grupo16.models.dtos.responses.DisponibilidadResponseDTO;
import com.unla.grupo16.models.dtos.responses.TurnoPreviewResponseDTO;
import com.unla.grupo16.models.dtos.responses.TurnoResponseDTO;
import com.unla.grupo16.models.entities.Cliente;
import com.unla.grupo16.models.entities.Servicio;
import com.unla.grupo16.models.entities.Turno;

public interface ITurnoService {

    //////// ok
    
    List<LocalDate> traerDiasDisponiblesParaServicio(Servicio servicio);

    List<String> traerHorariosDisponiblesParaServicio(Integer servicioId, LocalDate fecha);

    TurnoPreviewResponseDTO generarPreview(TurnoRequestDTO dto);

    TurnoResponseDTO crearTurno(TurnoRequestDTO dto, Cliente cliente, String username) throws NegocioException;

    void cancelarTurnoPorCodigo(String codigo) throws NegocioException;

    DisponibilidadResponseDTO traerDisponibilidadPorDiaYServicio(LocalDate fecha, Integer servicioId);

    List<String> traerFechasHabilitadasPorMes(Servicio servicio, int year, int month);

    List<Turno> traerTurnosNoDisponibles();

    List<TurnoResponseDTO> obtenerTurnosPorCliente(Integer clienteId);

}
