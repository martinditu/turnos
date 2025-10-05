package com.unla.grupo16.repositories;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.grupo16.models.entities.Disponibilidad;
import com.unla.grupo16.models.entities.Servicio;

@Repository
public interface IDisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {

    // OK 
    // CLIENTE
    List<Disponibilidad> findByServiciosContaining(Servicio servicio);

    List<Disponibilidad> findByServicios_IdAndDiaSemana(Integer servicioId, DayOfWeek diaSemana);

    List<Disponibilidad> findByDiaSemanaAndServiciosId(DayOfWeek diaSemana, Integer servicioId);

    // DATALOADER
    Optional<Disponibilidad> findByDiaSemanaAndHoraInicioAndHoraFin(DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin);

}
