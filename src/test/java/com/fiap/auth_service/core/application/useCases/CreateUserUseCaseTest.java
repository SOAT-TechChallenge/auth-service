package com.fiap.auth_service.core.application.useCases;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.application.dto.UserInputDTO;
import com.fiap.auth_service.core.gateways.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void execute_ShouldCreateUser_WhenUserDoesNotExist() {
        UserInputDTO inputDto = new UserInputDTO("testuser", "rawPassword", "test@email.com");
        String encodedPassword = "encodedPassword123";
        User expectedSavedUser = User.build(UUID.randomUUID(), "testuser", encodedPassword, "test@email.com");

        when(userGateway.findByUsername(inputDto.username())).thenReturn(null);
        when(passwordEncoder.encode(inputDto.password())).thenReturn(encodedPassword);
        when(userGateway.save(any(User.class))).thenReturn(expectedSavedUser);

        User result = createUserUseCase.execute(inputDto);

        assertNotNull(result);
        assertEquals(expectedSavedUser.getId(), result.getId());
        assertEquals(encodedPassword, result.getPassword());
        
        verify(userGateway).findByUsername(inputDto.username());
        verify(passwordEncoder).encode(inputDto.password());
        verify(userGateway).save(any(User.class));
    }

    @Test
    void execute_ShouldThrowException_WhenUserAlreadyExists() {
        UserInputDTO inputDto = new UserInputDTO("existingUser", "pass", "email@test.com");
        User existingUser = User.build(UUID.randomUUID(), "existingUser", "pass", "email@test.com");

        when(userGateway.findByUsername(inputDto.username())).thenReturn(existingUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            createUserUseCase.execute(inputDto)
        );

        assertEquals("Usuário já existe", exception.getMessage());

        verify(userGateway).findByUsername(inputDto.username());
        verifyNoInteractions(passwordEncoder);
        verify(userGateway, never()).save(any());
    }
}