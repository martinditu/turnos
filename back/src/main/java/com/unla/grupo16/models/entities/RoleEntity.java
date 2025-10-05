package com.unla.grupo16.models.entities;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.unla.grupo16.models.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_rol", nullable = false, length = 80, unique = true)
    private RoleType type;

    @Column(name = "fechaC_rol")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "fechaM_rol")
    @UpdateTimestamp
    private Timestamp updateAt;

    public RoleEntity(@NotNull RoleType type) {
        this.type = type;
    }
}