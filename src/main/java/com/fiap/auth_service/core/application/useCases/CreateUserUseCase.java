package com.fiap.auth_service.core.application.useCases;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.application.dto.UserInputDTO;
import com.fiap.auth_service.core.gateways.UserGateway;

public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;


    public CreateUserUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }
    

    public User execute(UserInputDTO dto) {

        if (userGateway.findByUsername(dto.username()) != null) {
            throw new RuntimeException("Usuário já existe");
        }

        var user = User.build(
                null,
                dto.username(),
                passwordEncoder.encode(dto.password()),
                dto.email()
        );

        return userGateway.save(user);
    }
}
