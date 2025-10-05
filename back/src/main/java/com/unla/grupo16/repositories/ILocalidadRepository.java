package com.unla.grupo16.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.grupo16.models.entities.Localidad;

@Repository
public interface ILocalidadRepository extends JpaRepository<Localidad, Integer> {

    // DATALOADER
    Optional<Localidad> findByNombre(String nombre);
}
