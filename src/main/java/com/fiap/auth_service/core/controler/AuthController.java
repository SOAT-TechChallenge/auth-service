package com.fiap.auth_service.core.controler;


import com.fiap.auth_service.core.application.dto.UserDTO;
import com.fiap.auth_service.core.application.useCases.FindUserByUsernameUseCase;
import com.fiap.auth_service.core.gateways.UserGateway;
import com.fiap.auth_service.core.gateways.UserGatewayImpl;
import com.fiap.auth_service.core.interfaces.UserDataSource;
import com.fiap.auth_service.core.presenter.UserPresenter;

public class AuthController {

    private final UserGateway userGateway;

    private AuthController(UserDataSource userDataSource) {
        this.userGateway = new UserGatewayImpl(userDataSource);
    }

    public static AuthController build(UserDataSource userDataSource) {
        return new AuthController(userDataSource);
    }

    public UserDTO validateByUsername(String username) {
        var useCase = new FindUserByUsernameUseCase(userGateway);

        var user = useCase.execute(username);
        return UserPresenter.toDTO(user);
    }
}
