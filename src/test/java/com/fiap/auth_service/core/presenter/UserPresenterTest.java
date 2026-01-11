package com.fiap.auth_service.core.presenter;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.application.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserPresenterTest {

    @Test
    void toDTO_ShouldConvertUserToUserDTO() {
        UUID id = UUID.randomUUID();
        String username = "testuser";
        String password = "password123";
        String email = "test@example.com";

        User user = User.build(id, username, password, email);

        UserDTO dto = UserPresenter.toDTO(user);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(username, dto.username());
        assertEquals(password, dto.password());
        assertEquals(email, dto.email());
    }
}