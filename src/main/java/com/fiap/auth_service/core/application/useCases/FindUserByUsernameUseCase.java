package com.fiap.auth_service.core.application.useCases;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.gateways.UserGateway;

public class FindUserByUsernameUseCase {

    private final UserGateway userGateway;

    public FindUserByUsernameUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(String username) {
        var userDTO = userGateway.findByUsername(username);

        if (userDTO == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        var user = User.build(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail()
        );

        return user;
    }
}
