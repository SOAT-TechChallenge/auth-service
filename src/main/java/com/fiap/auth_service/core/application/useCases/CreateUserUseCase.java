package com.fiap.auth_service.core.application.useCases;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.application.dto.UserInputDTO;
import com.fiap.auth_service.core.application.services.NotificationService;
import com.fiap.auth_service.core.gateways.UserGateway;

public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public CreateUserUseCase(NotificationService notificationService, UserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
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

        user = userGateway.save(user);

        notificationService.sendEmail(
                user.getEmail(),
                "Cadastro realizado no TechChallenge",
                String.format("Olá %s, seu cadastro no TechChallenge foi realizado! Seja bem vindo!", user.getUsername())
        );

        return user;
    }
}
