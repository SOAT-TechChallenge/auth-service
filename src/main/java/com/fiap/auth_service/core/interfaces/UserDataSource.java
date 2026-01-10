package com.fiap.auth_service.core.interfaces;

import com.fiap.auth_service.core.application.dto.UserDTO;

public interface UserDataSource {

    UserDTO save(UserDTO user);

    UserDTO findByUsername(String username);
}
