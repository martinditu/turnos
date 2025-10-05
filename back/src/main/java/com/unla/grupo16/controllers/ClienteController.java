package com.unla.grupo16.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.unla.grupo16.configurations.mapper.ServicioMapper;
import com.unla.grupo16.configurations.mapper.UbicacionMapper;
import com.unla.grupo16.exception.NegocioException;
import com.unla.grupo16.models.dtos.requests.TurnoRequestDTO;
import com.unla.grupo16.models.dtos.responses.DisponibilidadResponseDTO;
import com.unla.grupo16.models.dtos.responses.ServicioResponseDTO;
import com.unla.grupo16.models.dtos.responses.TurnoPreviewResponseDTO;
import com.unla.grupo16.models.dtos.responses.TurnoResponseDTO;
import com.unla.grupo16.models.dtos.responses.UbicacionResponseDTO;
import com.unla.grupo16.models.entities.Cliente;
import com.unla.grupo16.models.entities.Servicio;
import com.unla.grupo16.repositories.IServicioRepository;
import com.unla.grupo16.repositories.IUserRepository;
import com.unla.grupo16.services.interfaces.ITurnoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CLIENTE", description = "Operaciones que pueden realizar los clientes autenticados")
@RestController
@RequestMapping("/api/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteController {

    private final IServicioRepository servicioRepository;
    private final IUserRepository userRepository;

    private final ITurnoService turnoService;

    private final ServicioMapper servicioMapper;
    private final UbicacionMapper ubicacionMapper;

    public ClienteController(
            IServicioRepository servicioRepository,
            ITurnoService turnoService,
            IUserRepository userRepository,
            ServicioMapper servicioMapper,
            UbicacionMapper ubicacionMapper
    ) {
        this.servicioRepository = servicioRepository;
        this.turnoService = turnoService;
        this.userRepository = userRepository;
        this.servicioMapper = servicioMapper;
        this.ubicacionMapper = ubicacionMapper;
    }

    // OK
    @Operation(summary = "Traer todos los servicios disponibles")
    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioResponseDTO>> traerServicios() {
        List<Servicio> servicios = servicioRepository.findAll();
        return ResponseEntity.ok(servicioMapper.toDTOList(servicios));
    }

    // OK
    @Operation(summary = "Traer ubicaciones asociadas a un servicio")
    @GetMapping("/servicios/{servicioId}/ubicaciones")
    public ResponseEntity<List<UbicacionResponseDTO>> traerUbicaciones(
            @Parameter(description = "ID del servicio") @PathVariable Integer servicioId) {
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));

        return ResponseEntity.ok(ubicacionMapper.toDTOList(servicio.getUbicaciones()));
    }

    // OK
    @Operation(summary = "Traer los dias disponibles para un servicio")
    @GetMapping("/servicios/{servicioId}/dias-disponibles")
    public ResponseEntity<List<LocalDate>> traerDias(
            @Parameter(description = "ID del servicio") @PathVariable Integer servicioId) {
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));

        List<LocalDate> dias = turnoService.traerDiasDisponiblesParaServicio(servicio);
        return ResponseEntity.ok(dias);
    }

    // OK no hace lo mismo que el otro ?
    @Operation(summary = "Traer horarios disponibles para una fecha y servicio")
    @GetMapping("/servicios/{servicioId}/fechas/{fecha}/horarios")
    public ResponseEntity<List<String>> horariosDisponibles(
            @Parameter(description = "ID del servicio") @PathVariable Integer servicioId,
            @Parameter(description = "Fecha del turno (aaaa-mm-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(turnoService.traerHorariosDisponiblesParaServicio(servicioId, fecha));
    }

    // OK
    @Operation(summary = "Preview del turno antes de confirmarlo")
    @PostMapping("/confirmar")
    public ResponseEntity<TurnoPreviewResponseDTO> confirmarDatos(@RequestBody TurnoRequestDTO dto) {
        TurnoPreviewResponseDTO preview = turnoService.generarPreview(dto);
        return ResponseEntity.ok(preview);
    }

    // OK
    @Operation(summary = "Crear un nuevo turno para el cliente autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Turno creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    })
    @PostMapping
    public ResponseEntity<TurnoResponseDTO> crearTurno(@RequestBody TurnoRequestDTO dto, Principal principal) {
        Cliente cliente = traerClienteAutenticado(principal);

        TurnoResponseDTO turno = turnoService.crearTurno(dto, cliente, principal.getName());

        return ResponseEntity.ok(turno);
    }

    // OK
    @Operation(summary = "Anular un turno utilizando el código de anulación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Turno anulado con éxito"),
        @ApiResponse(responseCode = "400", description = "Error de negocio (ej: el turno ya estaba disponible)")
    })
    @PostMapping("/anular")
    public ResponseEntity<Map<String, Object>> anularTurno(
            @Parameter(description = "Código de anulación enviado por email")
            @RequestParam String codigoAnulacion) {
        try {
            turnoService.cancelarTurnoPorCodigo(codigoAnulacion);

            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("estado", 200);
            body.put("mensaje", "Turno anulado con éxito");
            return ResponseEntity.ok(body);

        } catch (NegocioException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("estado", 400);
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // OK
    @Operation(summary = "Ver turnos del cliente autenticado")
    @GetMapping("/mis-turnos")
    public ResponseEntity<List<TurnoResponseDTO>> traerTurnosDelCliente(Principal principal) {
        Cliente cliente = traerClienteAutenticado(principal);
        return ResponseEntity.ok(turnoService.obtenerTurnosPorCliente(cliente.getId()));
    }

    @Operation(summary = "Ver disponibilidad de un servicio en una fecha puntual")
    @GetMapping("/servicio/{servicioId}/disponibilidad/{fecha}")
    public ResponseEntity<DisponibilidadResponseDTO> getDisponibilidadPorDia(
            @PathVariable Integer servicioId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        DisponibilidadResponseDTO dto = turnoService.traerDisponibilidadPorDiaYServicio(fecha, servicioId);
        return ResponseEntity.ok(dto);
    }

    // OK
    @Operation(summary = "Obtener fechas habilitadas de un servicio en un mes especifico")
    @GetMapping("/servicios/{servicioId}/fechas-habilitadas")
    public ResponseEntity<List<String>> traerFechasHabilitadas(
            @PathVariable Integer servicioId,
            @RequestParam Integer year,
            @RequestParam Integer month) {

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));

        List<String> fechasHabilitadas = turnoService.traerFechasHabilitadasPorMes(servicio, year, month);

        return ResponseEntity.ok(fechasHabilitadas);
    }

    // OK
    // Interno
    private Cliente traerClienteAutenticado(Principal principal) {
        return userRepository.findByEmailOnlyIfHasPersona(principal.getName())
                .map(user -> Hibernate.unproxy(user.getPersona()))
                .filter(Cliente.class::isInstance)
                .map(Cliente.class::cast)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Perfil de cliente requerido"));
    }
}
