package com.fiap.auth_service.core.controler;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fiap.auth_service.core.application.dto.UserDTO;
import com.fiap.auth_service.core.application.dto.UserInputDTO;
import com.fiap.auth_service.core.application.services.NotificationService;
import com.fiap.auth_service.core.application.useCases.CreateUserUseCase;
import com.fiap.auth_service.core.application.useCases.FindUserByUsernameUseCase;
import com.fiap.auth_service.core.gateways.NotificationGateway;
import com.fiap.auth_service.core.gateways.NotificationGatewayImpl;
import com.fiap.auth_service.core.gateways.UserGateway;
import com.fiap.auth_service.core.gateways.UserGatewayImpl;
import com.fiap.auth_service.core.interfaces.NotificationDataSource;
import com.fiap.auth_service.core.interfaces.UserDataSource;
import com.fiap.auth_service.core.presenter.UserPresenter;

public class UserController {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    private UserController(NotificationDataSource notificationDataSource, UserDataSource userDataSource, PasswordEncoder passwordEncoder) {
        NotificationGateway notificationGateway = new NotificationGatewayImpl(notificationDataSource);
        this.notificationService = new NotificationService(notificationGateway);
        this.userGateway = new UserGatewayImpl(userDataSource);
        this.passwordEncoder = passwordEncoder;
    }

    public static UserController build(NotificationDataSource notificationDataSource, UserDataSource userDataSource, PasswordEncoder passwordEncoder) {
        return new UserController(notificationDataSource, userDataSource, passwordEncoder);
    }

    public UserDTO create(UserInputDTO dto) {
        var useCase = new CreateUserUseCase(notificationService, userGateway, passwordEncoder);

        var user = useCase.execute(dto);
        return UserPresenter.toDTO(user);
    }

    public UserDTO findByUsername(String username) {
        var useCase = new FindUserByUsernameUseCase(userGateway);

        var user = useCase.execute(username);
        return UserPresenter.toDTO(user);
    }

}
