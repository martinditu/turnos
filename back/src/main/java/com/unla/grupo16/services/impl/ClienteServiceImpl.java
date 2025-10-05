package com.unla.grupo16.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.unla.grupo16.configurations.mapper.ClienteMapper;
import com.unla.grupo16.exception.NegocioException;
import com.unla.grupo16.exception.RecursoNoEncontradoException;
import com.unla.grupo16.models.dtos.requests.RegistroClienteRequestDTO;
import com.unla.grupo16.models.dtos.responses.ClienteAdminDTO;
import com.unla.grupo16.models.dtos.responses.ClientesAdminResponseDTO;
import com.unla.grupo16.models.entities.Cliente;
import com.unla.grupo16.models.entities.RoleEntity;
import com.unla.grupo16.models.entities.UserEntity;
import com.unla.grupo16.models.enums.RoleType;
import com.unla.grupo16.repositories.IClienteRepository;
import com.unla.grupo16.repositories.IRoleRepository;
import com.unla.grupo16.repositories.IUserRepository;
import com.unla.grupo16.services.interfaces.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {

    private final IUserRepository userRepository;
    private final IClienteRepository clienteRepository;
    private final IRoleRepository roleRepository; // /*pattern return: Para asignar rol CLIENTE */
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder; // /*pattern return: Para encriptar contraseñas */

    public ClienteServiceImpl(IUserRepository userRepository, IClienteRepository clienteRepository, 
                             IRoleRepository roleRepository, ClienteMapper clienteMapper,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.roleRepository = roleRepository;
        this.clienteMapper = clienteMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // OK
    // ADMIN
    @Override
    public ClientesAdminResponseDTO traerClientesActivosYBajaLogica() {
        List<UserEntity> usuarios = userRepository.findAllClientesConUsuarioIncluyendoBaja();

        Map<Boolean, List<ClienteAdminDTO>> particionados = usuarios.stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.partitioningBy(ClienteAdminDTO::clienteActivo)); // partition divide en dos listas

        return new ClientesAdminResponseDTO(
                particionados.get(true), // clientes activos
                particionados.get(false) // baja lógica
        );
    }

    @Override
    public void darDeBajaCliente(Integer clienteId) throws NegocioException, RecursoNoEncontradoException {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        boolean tieneTurnosActivos = cliente.getTurnos().stream()
                .anyMatch(turno -> !turno.isDisponible());

        if (tieneTurnosActivos) {
            throw new NegocioException("No se puede dar de baja un cliente con turnos activos");
        }

        UserEntity user = userRepository.findByPersona(cliente)
                .orElseThrow(() -> new NegocioException("Usuario asociado al cliente no encontrado"));

        user.setActivo(false);
        userRepository.save(user);
    }

    @Override
    public void darDeAltaCliente(Integer clienteId) throws RecursoNoEncontradoException {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        UserEntity user = userRepository.findByPersona(cliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario asociado al cliente no encontrado"));

        user.setActivo(true);
        userRepository.save(user);
    }

    @Override
    public ClienteAdminDTO editarCliente(Integer clienteId, ClienteAdminDTO dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        UserEntity user = userRepository.findByPersona(cliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario asociado al cliente no encontrado"));

        cliente.setNombre(dto.nombre());
        cliente.setApellido(dto.apellido());
        cliente.setDni(dto.dni());
        user.setEmail(dto.email());

        clienteRepository.save(cliente);
        userRepository.save(user);

        return clienteMapper.toDTO(user);
    }

    /*pattern return: Implementación del registro de nuevos clientes */
    /* Crea tanto la entidad Cliente como el UserEntity asociado */
    /* Asigna automáticamente el rol CLIENTE y encripta la contraseña */
    @Override
    @Transactional
    public void registrarNuevoCliente(RegistroClienteRequestDTO registro) {
        /*pattern return: Validar que el email no esté ya registrado */
        if (userRepository.findByEmail(registro.email()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado en el sistema");
        }

        /*pattern return: Obtener el rol CLIENTE desde la base de datos */
        RoleEntity rolCliente = roleRepository.findByType(RoleType.CLIENTE)
                .orElseThrow(() -> new IllegalStateException("Rol CLIENTE no encontrado en el sistema"));

        /*pattern return: Crear la entidad Cliente con los datos personales */
        Cliente cliente = new Cliente();
        cliente.setNombre(registro.nombre());
        cliente.setApellido(registro.apellido());
        cliente.setTelefono(registro.telefono());
        // DNI puede ser null inicialmente, se puede agregar después
        cliente.setActivo(true);

        /*pattern return: Crear el UserEntity vinculado al Cliente */
        UserEntity usuario = new UserEntity();
        usuario.setEmail(registro.email());
        usuario.setPassword(passwordEncoder.encode(registro.password())); // /*pattern return: Encripta la contraseña */
        usuario.setActivo(true);
        usuario.setRoleEntities(List.of(rolCliente));
        usuario.setPersona(cliente); // /*pattern return: Vincula el usuario con el cliente */

        /*pattern return: Guardar en la base de datos */
        clienteRepository.save(cliente);
        userRepository.save(usuario);
    }

}
