package com.fiap.auth_service.core.gateways;

import com.fiap.auth_service.core.application.domain.entities.User;

public interface UserGateway {

    User save(User user);

    User findByUsername(String username);
}
