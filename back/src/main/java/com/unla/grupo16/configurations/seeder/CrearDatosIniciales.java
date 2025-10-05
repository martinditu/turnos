package com.unla.grupo16.configurations.seeder;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.unla.grupo16.models.entities.Disponibilidad;
import com.unla.grupo16.models.entities.Empleado;
import com.unla.grupo16.models.entities.Localidad;
import com.unla.grupo16.models.entities.Servicio;
import com.unla.grupo16.models.entities.Ubicacion;
import com.unla.grupo16.repositories.IDisponibilidadRepository;
import com.unla.grupo16.repositories.IEmpleadoRepository;
import com.unla.grupo16.repositories.ILocalidadRepository;
import com.unla.grupo16.repositories.IServicioRepository;
import com.unla.grupo16.repositories.IUbicacionRepository;

@Component
public class CrearDatosIniciales implements CommandLineRunner {

    private static final String LOCALIDAD_NOMBRE = "Lomas de Zamora";
    private static final String DIRECCION = "Alver 345";
    private static final String SERVICIO_NOMBRE = "VTV";
    private static final int DURACION_MINUTOS = 45;
    private static final String DNI_EMPLEADO = "39999999";

    private final IServicioRepository servicioRepo;
    private final IDisponibilidadRepository disponibilidadRepo;
    private final IEmpleadoRepository empleadoRepo;
    private final ILocalidadRepository localidadRepo;
    private final IUbicacionRepository ubicacionRepo;

    public CrearDatosIniciales(IServicioRepository servicioRepo,
            IDisponibilidadRepository disponibilidadRepo,
            IEmpleadoRepository empleadoRepo,
            ILocalidadRepository localidadRepo,
            IUbicacionRepository ubicacionRepo) {
        this.servicioRepo = servicioRepo;
        this.disponibilidadRepo = disponibilidadRepo;
        this.empleadoRepo = empleadoRepo;
        this.localidadRepo = localidadRepo;
        this.ubicacionRepo = ubicacionRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        Localidad localidad = crearLocalidadSiNoExiste();
        Ubicacion ubicacion = crearUbicacionSiNoExiste(localidad);
        Servicio servicio = crearServicioSiNoExiste(ubicacion);
        crearDisponibilidades(servicio);
        crearEmpleadoSiNoExiste();
    }

    private Localidad crearLocalidadSiNoExiste() {
        return localidadRepo.findByNombre(LOCALIDAD_NOMBRE).orElseGet(() -> {
            Localidad nueva = new Localidad();
            nueva.setNombre(LOCALIDAD_NOMBRE);
            return localidadRepo.save(nueva);
        });
    }

    private Ubicacion crearUbicacionSiNoExiste(Localidad localidad) {
        return ubicacionRepo.findByDireccion(DIRECCION).orElseGet(() -> {
            Ubicacion nueva = new Ubicacion();
            nueva.setDireccion(DIRECCION);
            nueva.setLocalidad(localidad);
            return ubicacionRepo.save(nueva);
        });
    }

    private Servicio crearServicioSiNoExiste(Ubicacion ubicacion) {
        return servicioRepo.findByNombre(SERVICIO_NOMBRE).orElseGet(() -> {
            Servicio nuevo = Servicio.builder()
                    .nombre(SERVICIO_NOMBRE)
                    .duracion(DURACION_MINUTOS)
                    .ubicaciones(Set.of(ubicacion))
                    .build();
            return servicioRepo.save(nuevo);
        });
    }

    private void crearDisponibilidades(Servicio servicio) {
        List<DayOfWeek> diasLaborales = List.of(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
        );

        for (DayOfWeek dia : diasLaborales) {
            LocalTime hora = LocalTime.of(8, 0);
            LocalTime fin = LocalTime.of(18, 0);

            while (hora.plusMinutes(servicio.getDuracion()).compareTo(fin) <= 0) {
                LocalTime horaIni = hora;
                LocalTime horaFin = hora.plusMinutes(servicio.getDuracion());

                Disponibilidad disponibilidad = disponibilidadRepo
                        .findByDiaSemanaAndHoraInicioAndHoraFin(dia, horaIni, horaFin)
                        .orElseGet(() -> disponibilidadRepo.save(
                        Disponibilidad.builder()
                                .diaSemana(dia)
                                .horaInicio(horaIni)
                                .horaFin(horaFin)
                                .build()
                ));

                if (!disponibilidad.getServicios().contains(servicio)) {
                    disponibilidad.getServicios().add(servicio);
                    disponibilidadRepo.save(disponibilidad);
                }

                hora = hora.plusMinutes(servicio.getDuracion());
            }
        }
    }

    private void crearEmpleadoSiNoExiste() {
        empleadoRepo.findByDni(DNI_EMPLEADO).orElseGet(() -> {
            Empleado empleado = new Empleado();
            empleado.setNombre("Alberto");
            empleado.setApellido("Perez");
            empleado.setDni(DNI_EMPLEADO);
            empleado.setLegajo("LEG001");
            return empleadoRepo.save(empleado);
        });
    }
}
