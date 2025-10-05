package com.unla.grupo16.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.grupo16.models.entities.RoleEntity;
import com.unla.grupo16.models.enums.RoleType;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Integer> {

    // USERS SEEDER
    Optional<RoleEntity> findByType(RoleType type);

}
