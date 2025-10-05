package com.unla.grupo16.services.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.unla.grupo16.configurations.mapper.ServicioMapper;
import com.unla.grupo16.configurations.mapper.TurnoMapper;
import com.unla.grupo16.configurations.mapper.UbicacionMapper;
import com.unla.grupo16.exception.DisponibilidadNoEncontradaException;
import com.unla.grupo16.exception.NegocioException;
import com.unla.grupo16.exception.RecursoNoEncontradoException;
import com.unla.grupo16.models.dtos.requests.TurnoRequestDTO;
import com.unla.grupo16.models.dtos.responses.DisponibilidadResponseDTO;
import com.unla.grupo16.models.dtos.responses.TurnoPreviewResponseDTO;
import com.unla.grupo16.models.dtos.responses.TurnoResponseDTO;
import com.unla.grupo16.models.entities.Cliente;
import com.unla.grupo16.models.entities.Disponibilidad;
import com.unla.grupo16.models.entities.Empleado;
import com.unla.grupo16.models.entities.Servicio;
import com.unla.grupo16.models.entities.Turno;
import com.unla.grupo16.models.entities.Ubicacion;
import com.unla.grupo16.repositories.IClienteRepository;
import com.unla.grupo16.repositories.IDisponibilidadRepository;
import com.unla.grupo16.repositories.IEmpleadoRepository;
import com.unla.grupo16.repositories.IServicioRepository;
import com.unla.grupo16.repositories.ITurnoRepository;
import com.unla.grupo16.repositories.IUbicacionRepository;
import com.unla.grupo16.services.interfaces.ITurnoService;

import jakarta.mail.MessagingException;

@Service
public class TurnoServiceImpl implements ITurnoService {

    private final ITurnoRepository turnoRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final EmailServiceImpl emailService;
    private final IServicioRepository servicioRepository;
    private final IDisponibilidadRepository disponibilidadRepository;
    private final IUbicacionRepository ubicacionRepository;

    private final TurnoMapper turnoMapper;
    private final ServicioMapper servicioMapper;
    private final UbicacionMapper ubicacionMapper;

    public TurnoServiceImpl(ITurnoRepository turnoRepository,
            IClienteRepository clienteRepository,
            IEmpleadoRepository empleadoRepository,
            IServicioRepository servicioRepository,
            IDisponibilidadRepository disponibilidadRepository,
            EmailServiceImpl emailService,
            IUbicacionRepository ubicacionRepository,
            TurnoMapper turnoMapper,
            ServicioMapper servicioMapper,
            UbicacionMapper ubicacionMapper) {
        this.turnoRepository = turnoRepository;
        this.empleadoRepository = empleadoRepository;
        this.servicioRepository = servicioRepository;
        this.disponibilidadRepository = disponibilidadRepository;
        this.emailService = emailService;
        this.ubicacionRepository = ubicacionRepository;
        this.turnoMapper = turnoMapper;
        this.servicioMapper = servicioMapper;
        this.ubicacionMapper = ubicacionMapper;
    }

    @Override
    public TurnoPreviewResponseDTO generarPreview(TurnoRequestDTO dto) {
        Servicio servicio = servicioRepository.findById(dto.idServicio())
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado"));

        Ubicacion ubicacion = ubicacionRepository.findById(dto.idUbicacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Ubicacion no encontrada"));

        return new TurnoPreviewResponseDTO(
                servicioMapper.toDTO(servicio),
                ubicacionMapper.toDTO(ubicacion),
                dto.fecha(),
                dto.hora()
        );
    }

    @Override
    public List<TurnoResponseDTO> obtenerTurnosPorCliente(Integer clienteId) {
        List<Turno> turnos = turnoRepository.findByClienteIdAndDisponibleFalse(clienteId); // solo los turnos reservados
        return turnos.stream()
                .map(turnoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --------------------------------------------------------------------
    // OK
    // CLIENTE
    // metodo para return una lista de fechas LocalDate de los prox 30dias en los q un servicio tiene disponibilidad habilitada y con horarios libres
    @Override
    public List<LocalDate> traerDiasDisponiblesParaServicio(Servicio servicio) {

        // obtiene los dias de la semana con disp para ese serv
        // set para evitar duplicados(varias disp el mismo dia)
        Set<DayOfWeek> diasDisponibles = disponibilidadRepository.findByServiciosContaining(servicio).stream()
                .map(Disponibilidad::getDiaSemana)
                .collect(Collectors.toSet());

        // rango de fechas
        LocalDate hoy = LocalDate.now();
        LocalDate fin = hoy.plusDays(30);

        // filtra fechas
        return hoy.datesUntil(fin.plusDays(1)) // stren localdate desde hoy hasta fin + 1 = 30
                .filter(fecha -> diasDisponibles.contains(fecha.getDayOfWeek())) // fltra dias disp
                .filter(fecha -> !traerHorariosDisponiblesParaServicio(servicio.getId(), fecha).isEmpty()) // filtra horarios para ese dia 
                .toList();
    }

    @Override
    // devuelve una lista de horarios disp como string para un serv en una fecha especifica, teniendo en cuanta la disp y turnos reservados
    public List<String> traerHorariosDisponiblesParaServicio(Integer servicioId, LocalDate fecha) {

        // valida q id y fecha no sean null
        validarEntrada(servicioId, fecha);

        // si la fecha es anterior a hoy, devuelve lista vacia
        if (fecha.isBefore(LocalDate.now())) {
            return List.of();
        }

        // obtener las disp para ese dia de la semana
        DayOfWeek dia = fecha.getDayOfWeek();

        List<Disponibilidad> disponibilidades = disponibilidadRepository.findByServicios_IdAndDiaSemana(servicioId, dia);

        // cada disp tiene un rango horario, genero todos los horarios por cada disp 
        List<LocalTime> horariosGenerados = disponibilidades.stream()
                .flatMap(d -> generarHorarios(d.getHoraInicio(), d.getHoraFin()).stream())
                .toList();

        Set<String> horariosOcupados = turnoRepository.findByServicioIdAndFecha(servicioId, fecha.atStartOfDay(), fecha.plusDays(1).atStartOfDay())
                .stream()
                .map(t -> t.getFechaHora().toLocalTime().toString())
                .collect(Collectors.toSet());

        LocalTime ahora = fecha.equals(LocalDate.now()) ? LocalTime.now().withSecond(0).withNano(0) : null;

        return horariosGenerados.stream()
                .filter(h -> !horariosOcupados.contains(h.toString()))
                .filter(h -> ahora == null || h.isAfter(ahora))
                .map(LocalTime::toString)
                .toList();
    }

    private void validarEntrada(Integer servicioId, LocalDate fecha) {
        if (servicioId == null) {
            throw new IllegalArgumentException("El servicio_id no puede ser null");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser null");
        }
    }

    private List<LocalTime> generarHorarios(LocalTime inicio, LocalTime fin) {
        List<LocalTime> horarios = new ArrayList<>();
        while (!inicio.plusMinutes(30).isAfter(fin)) {
            horarios.add(inicio);
            inicio = inicio.plusMinutes(30);
        }
        return horarios;
    }

    @Override
    public TurnoResponseDTO crearTurno(TurnoRequestDTO dto, Cliente cliente, String username) {
        validarTurnoRequest(dto);

        Servicio servicio = servicioRepository.findById(dto.idServicio())
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado"));

        LocalDateTime fechaHora = LocalDateTime.of(dto.fecha(), dto.hora());

        if (fechaHora.isBefore(LocalDateTime.now().withSecond(0).withNano(0))) {
            throw new NegocioException("No se pueden reservar turnos en el pasado");
        }

        Empleado empleado = buscarEmpleadoDisponible(fechaHora)
                .orElseThrow(() -> new DisponibilidadNoEncontradaException("No hay empleados disponibles para esa fecha y hora"));

        Turno turno = crearTurnoDesdeDTO(dto, cliente, servicio, empleado, fechaHora);

        turnoRepository.save(turno);

        enviarEmailConfirmacion(username, turno);

        return turnoMapper.toDTO(turno);
    }

    private void validarTurnoRequest(TurnoRequestDTO dto) throws NegocioException {
        if (dto.idServicio() == null) {
            throw new NegocioException("El idServicio es obligatorio");
        }
        if (dto.fecha() == null) {
            throw new NegocioException("La fecha es obligatoria");
        }
        if (dto.hora() == null) {
            throw new NegocioException("La hora es obligatoria");
        }
    }

    private Optional<Empleado> buscarEmpleadoDisponible(LocalDateTime fechaHora) {
        return empleadoRepository.findAll().stream()
                .filter(emp -> !turnoRepository.existsByEmpleadoAndFechaHoraAndDisponibleFalse(emp, fechaHora))
                .findFirst();
    }

    private Turno crearTurnoDesdeDTO(TurnoRequestDTO dto, Cliente cliente, Servicio servicio,
            Empleado empleado, LocalDateTime fechaHora) {
        Turno turno = turnoMapper.toEntity(dto);
        turno.setCliente(cliente);
        turno.setEmpleado(empleado);
        turno.setServicio(servicio);
        turno.setFechaHora(fechaHora);
        turno.setDisponible(false);
        turno.setCodigoAnulacion(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return turno;
    }

    private void enviarEmailConfirmacion(String emailCliente, Turno turno) throws NegocioException {
        try {
            emailService.sendEmail(emailCliente, turno);
        } catch (MessagingException e) {
            throw new NegocioException("Error al enviar email de confirmacion: " + e.getMessage());
        }
    }

    @Override
    public void cancelarTurnoPorCodigo(String codigoAnulacion) throws NegocioException {
        Turno turno = turnoRepository.findByCodigoAnulacion(codigoAnulacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado con ese codigo."));

        anularTurno(turno);
    }

    private void anularTurno(Turno turno) throws NegocioException {
        if (turno.isDisponible()) {
            throw new NegocioException("El turno ya fue anulado o ya esta disponible.");
        }

        turno.setEmpleado(null);
        turno.setDisponible(true);
        turno.setCliente(null);
        turno.setCodigoAnulacion(null);

        turnoRepository.save(turno);
    }

    @Override
    public DisponibilidadResponseDTO traerDisponibilidadPorDiaYServicio(LocalDate fecha, Integer servicioId) {

        DayOfWeek diaSemana = fecha.getDayOfWeek();
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now().withSecond(0).withNano(0);

        // tra disp por dia y serv
        List<Disponibilidad> disponibilidades = disponibilidadRepository.findByDiaSemanaAndServiciosId(diaSemana, servicioId);

        // verificar dia activo 
        boolean activo = !disponibilidades.isEmpty() && !fecha.isBefore(hoy);

        // filtra horarios pasados
        Stream<Disponibilidad> stream = disponibilidades.stream();
        if (fecha.isEqual(hoy)) {
            stream = stream.filter(d -> d.getHoraFin().isAfter(ahora));
        }
        // mapear horarios, ajustando horaInicio si corresponde
        List<DisponibilidadResponseDTO.HorarioDTO> horarios = stream
                .map(d -> {
                    LocalTime inicio = d.getHoraInicio();
                    LocalTime fin = d.getHoraFin();

                    if (fecha.isEqual(hoy) && inicio.isBefore(ahora)) {
                        inicio = ahora;
                    }

                    // valida ranfo correctos
                    return inicio.isBefore(fin) || inicio.equals(fin)
                            ? new DisponibilidadResponseDTO.HorarioDTO(inicio, fin)
                            : null;
                })
                .filter(Objects::nonNull)
                .toList();

        return new DisponibilidadResponseDTO(fecha, activo, horarios);

    }

    @Override
    public List<String> traerFechasHabilitadasPorMes(Servicio servicio, int year, int month) {
        // fecha inicio: primer dia del mes y año dados
        LocalDate inicio = LocalDate.of(year, month, 1);

        // fecha cha fin: ultimo dia del mes 
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());

        // tra todas las fechas disp para el serv
        List<LocalDate> fechasDisponibles = traerDiasDisponiblesParaServicio(servicio).stream()
                // filtra fecha para el mes seleccionado
                .filter(dia -> !dia.isBefore(inicio) && !dia.isAfter(fin))
                .toList();

        //convierte a yyy-mm-dd
        return fechasDisponibles.stream()
                .map(LocalDate::toString)
                .toList();
    }

    // --------------------------------------------------------------------------------
    // ADMIN
    @Override
    public List<Turno> traerTurnosNoDisponibles() {
        return turnoRepository.findByDisponibleFalseOrderByFechaHoraAsc();
    }

}
