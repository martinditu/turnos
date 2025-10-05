package com.unla.grupo16.configurations.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.unla.grupo16.models.dtos.requests.TurnoRequestDTO;
import com.unla.grupo16.models.dtos.responses.TurnoResponseDTO;
import com.unla.grupo16.models.entities.Servicio;
import com.unla.grupo16.models.entities.Turno;

@Component
public class TurnoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public TurnoResponseDTO toDTO(Turno turno) {
        LocalDateTime fechaHora = turno.getFechaHora();

        String fecha = fechaHora != null ? fechaHora.toLocalDate().format(DATE_FORMATTER) : null;
        String hora = fechaHora != null ? fechaHora.toLocalTime().format(TIME_FORMATTER) : null;

        String nombreServicio = turno.getServicio() != null ? turno.getServicio().getNombre() : null;
        String nombreEmpleado = turno.getEmpleado() != null ? turno.getEmpleado().getNombre() : "A confirmar";
        String nombreCliente = turno.getCliente() != null ? turno.getCliente().getNombre() : null;

        String ubicacionDescripcion = "Sin ubicacion";
        Servicio servicio = turno.getServicio();
        if (servicio != null && servicio.getUbicaciones() != null && !servicio.getUbicaciones().isEmpty()) {
            ubicacionDescripcion = servicio.getUbicaciones().stream()
                    .findFirst()
                    .map(ubicacion -> {
                        String direccion = ubicacion.getDireccion();
                        String localidad = (ubicacion.getLocalidad() != null)
                                ? ubicacion.getLocalidad().getNombre()
                                : "Sin localidad";
                        return direccion + " (" + localidad + ")";
                    })
                    .orElse("Sin ubicacion");
        }

        return new TurnoResponseDTO(
                turno.getId(),
                fecha,
                hora,
                nombreServicio,
                nombreEmpleado,
                nombreCliente,
                ubicacionDescripcion,
                turno.getCodigoAnulacion()
        );
    }

    public Turno toEntity(TurnoRequestDTO dto) {
        Turno turno = new Turno();
        turno.setFechaHora(LocalDateTime.of(dto.fecha(), dto.hora()));
        turno.setDisponible(false); // reservado por defecto
        return turno;
    }

}
