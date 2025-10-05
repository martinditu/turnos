package com.unla.grupo16.services.impl;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.grupo16.repositories.IUserRepository;
import com.unla.grupo16.services.interfaces.IUserService;

@Service("userService")
public class UserServiceImp implements IUserService {

    private final IUserRepository userRepository;

    public UserServiceImp(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // jwt
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailWithPersona(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isActivo(),
                true,
                true,
                true,
                user.getRoleEntities().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getType().name()))
                        .collect(Collectors.toList())
        ))
                .orElseThrow(()
                        -> new UsernameNotFoundException("Usuario con email " + email + " no encontrado"));
    }
}
