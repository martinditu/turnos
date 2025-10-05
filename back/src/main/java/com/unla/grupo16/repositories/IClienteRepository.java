package com.unla.grupo16.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.grupo16.models.entities.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Integer> {

}
