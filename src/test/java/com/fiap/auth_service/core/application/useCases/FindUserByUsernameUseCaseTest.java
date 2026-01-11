package com.fiap.auth_service.core.application.useCases;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.gateways.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByUsernameUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private FindUserByUsernameUseCase findUserByUsernameUseCase;

    @Test
    void execute_ShouldReturnUser_WhenUserExists() {
        String username = "testuser";
        UUID id = UUID.randomUUID();
        User foundUser = User.build(id, username, "password123", "test@email.com");

        when(userGateway.findByUsername(username)).thenReturn(foundUser);

        User result = findUserByUsernameUseCase.execute(username);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals("test@email.com", result.getEmail());
        verify(userGateway, times(1)).findByUsername(username);
    }

    @Test
    void execute_ShouldThrowException_WhenUserDoesNotExist() {
        String username = "nonexistent";

        when(userGateway.findByUsername(username)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            findUserByUsernameUseCase.execute(username)
        );

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(userGateway, times(1)).findByUsername(username);
    }
}