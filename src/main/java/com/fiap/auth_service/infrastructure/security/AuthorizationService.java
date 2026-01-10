package com.fiap.auth_service.infrastructure.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fiap.auth_service._webApi.data.persistence.repository.JpaUserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private JpaUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        return user;
    }
}
