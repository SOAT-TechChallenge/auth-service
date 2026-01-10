package com.fiap.auth_service.core.gateways;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.application.dto.UserDTO;
import com.fiap.auth_service.core.interfaces.UserDataSource;

public class UserGatewayImpl implements UserGateway {

    private final UserDataSource userDataSource;

    public UserGatewayImpl(UserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    @Override
    public User save(User user) {
        var userDTO = new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getEmail());

        var newUser = userDataSource.save(userDTO);

        return User.build(newUser.id(), newUser.username(), newUser.password(), newUser.email());
    }

    @Override
    public User findByUsername(String username) {
        var userDto = userDataSource.findByUsername(username);

        if(userDto == null) {
            return null;
        }

        return User.build(userDto.id(), userDto.username(), userDto.password(), userDto.email());
    }

}
