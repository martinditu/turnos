package com.unla.grupo16.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.grupo16.models.entities.Servicio;

@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Integer> {

    // DATALOADER
    Optional<Servicio> findByNombre(String nombre);
}
