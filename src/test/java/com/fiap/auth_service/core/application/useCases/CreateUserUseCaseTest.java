package com.fiap.auth_service.core.application.useCases;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.application.dto.UserInputDTO;
import com.fiap.auth_service.core.application.services.NotificationService; // <--- Importante: Service
import com.fiap.auth_service.core.gateways.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    // AGORA SIM: Mockamos o Service, pois é isso que o UseCase pede no construtor
    @Mock
    private NotificationService notificationService;

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void execute_ShouldCreateUser_WhenUserDoesNotExist() {
        // Arrange
        UserInputDTO inputDto = new UserInputDTO("testuser", "rawPassword", "test@email.com");
        String encodedPassword = "encodedPassword123";
        // Simulamos o retorno do banco com ID gerado
        User expectedSavedUser = User.build(UUID.randomUUID(), "testuser", encodedPassword, "test@email.com");

        when(userGateway.findByUsername(inputDto.username())).thenReturn(null);
        when(passwordEncoder.encode(inputDto.password())).thenReturn(encodedPassword);
        when(userGateway.save(any(User.class))).thenReturn(expectedSavedUser);

        // Act
        User result = createUserUseCase.execute(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSavedUser.getId(), result.getId());
        assertEquals(encodedPassword, result.getPassword());

        verify(userGateway).findByUsername(inputDto.username());
        verify(passwordEncoder).encode(inputDto.password());
        verify(userGateway).save(any(User.class));

        // Verificação ajustada para o método .sendEmail() do seu UseCase
        verify(notificationService, times(1))
            .sendEmail(
                eq("test@email.com"), 
                eq("Cadastro realizado no TechChallenge"), // Assunto exato do seu código
                contains("Seja bem vindo") // Verifica parte do corpo (mais seguro que string exata com formatação)
            );
    }

    @Test
    void execute_ShouldThrowException_WhenUserAlreadyExists() {
        // Arrange
        UserInputDTO inputDto = new UserInputDTO("existingUser", "pass", "email@test.com");
        User existingUser = User.build(UUID.randomUUID(), "existingUser", "pass", "email@test.com");

        when(userGateway.findByUsername(inputDto.username())).thenReturn(existingUser);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            createUserUseCase.execute(inputDto)
        );
        
        assertEquals("Usuário já existe", exception.getMessage());

        verify(userGateway).findByUsername(inputDto.username());
        
        verify(userGateway, never()).save(any());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(notificationService);
    }
}