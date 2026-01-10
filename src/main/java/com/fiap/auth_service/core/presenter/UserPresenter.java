package com.fiap.auth_service.core.presenter;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.application.dto.UserDTO;

public class UserPresenter {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getEmail()
        );
    }
}
