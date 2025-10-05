package com.unla.grupo16.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotBlank
    @Column(name = "nombre_persona")
    protected String nombre;

    @NotBlank
    @Column(name = "apellido_persona")
    private String apellido;

    @NotBlank
    @Column(name = "dni_persona")
    private String dni;

    // AUTH
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    public Integer getIdPersona() {
        return this.id;
    }
}
