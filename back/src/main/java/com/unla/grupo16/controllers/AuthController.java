package com.unla.grupo16.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unla.grupo16.configurations.security.jwt.JwtUtil;
import com.unla.grupo16.exception.AutenticacionException;
import com.unla.grupo16.exception.RecursoNoEncontradoException;
import com.unla.grupo16.exception.UsuarioDeshabilitadoException;
import com.unla.grupo16.models.dtos.requests.LoginRequestDTO;
import com.unla.grupo16.models.dtos.responses.LoginResponseDto;
import com.unla.grupo16.models.entities.UserEntity;
import com.unla.grupo16.repositories.IUserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "AUTENTICACION", description = "Autenticacion de usuarios mediante JWT")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final IUserRepository userRepo;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, IUserRepository userRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    @Operation(summary = "Autenticar usuario y generar JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autenticacion exitosa"),
        @ApiResponse(responseCode = "401", description = "Credenciales invalidas"),
        @ApiResponse(responseCode = "403", description = "Usuario deshabilitado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error inesperado")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // spring sec autentica al usuario
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            // reacupera los datos del usuario autenticado
            var userDetails = (UserDetails) auth.getPrincipal();

            // lo busca en la bd por persona 
            UserEntity usuario = userRepo.findByEmailWithPersona(userDetails.getUsername())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

            // no deja login si usuario no esta activo
            if (!usuario.isEnabled()) {
                throw new UsuarioDeshabilitadoException("El usuario esta deshabilitado");
            }

            // crea token con datos del usuario
            String token = jwtUtil.generarToken(userDetails);

            // pasa RoleType.ADMIN a ADMIN
            var rol = usuario.getRoleEntities().stream()
                    .findFirst()
                    .map(r -> r.getType().name())
                    .orElse("CLIENTE");

            // si hay persona usa su id, sino usa id de usuario
            var persona = usuario.getPersona();
            var id = persona != null ? persona.getIdPersona() : usuario.getId();
            var nombre = persona != null ? persona.getNombreCompleto() : usuario.getEmail();

            // respuesta
            var dto = new LoginResponseDto(
                    token,
                    usuario.getEmail(),
                    rol,
                    id,
                    nombre
            );

            return ResponseEntity.ok(dto);

        } catch (AuthenticationException e) {
            throw new AutenticacionException("Credenciales invalidas");
        }
    }
}
