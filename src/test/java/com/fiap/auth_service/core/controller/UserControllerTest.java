package com.fiap.auth_service.core.controller;

import com.fiap.auth_service.core.application.dto.UserDTO;
import com.fiap.auth_service.core.application.dto.UserInputDTO;
import com.fiap.auth_service.core.interfaces.UserDataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fiap.auth_service.core.controler.UserController;
import com.fiap.auth_service.core.interfaces.NotificationDataSource;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private NotificationDataSource notificationDataSource;

    @Mock
    private UserDataSource userDataSource;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = UserController.build(notificationDataSource, userDataSource, passwordEncoder);
    }

    @Test
    void create_ShouldReturnUserDTO_WhenUserIsCreatedSuccessfully() {
        UserInputDTO inputDto = new UserInputDTO("testuser", "123456", "test@email.com");
        UserDTO savedUserDto = new UserDTO(UUID.randomUUID(), "testuser", "encodedPass", "test@email.com");

        when(userDataSource.findByUsername(inputDto.username())).thenReturn(null);
        when(passwordEncoder.encode(inputDto.password())).thenReturn("encodedPass");
        when(userDataSource.save(any(UserDTO.class))).thenReturn(savedUserDto);

        UserDTO result = userController.create(inputDto);

        assertNotNull(result);
        assertEquals(savedUserDto.id(), result.id());
        assertEquals(savedUserDto.username(), result.username());
        assertEquals(savedUserDto.email(), result.email());

        verify(userDataSource).findByUsername(inputDto.username());
        verify(passwordEncoder).encode(inputDto.password());
        verify(userDataSource).save(any(UserDTO.class));
    }

    @Test
    void findByUsername_ShouldReturnUserDTO_WhenUserExists() {
        String username = "testuser";
        UserDTO foundUserDto = new UserDTO(UUID.randomUUID(), username, "encodedPass", "test@email.com");

        when(userDataSource.findByUsername(username)).thenReturn(foundUserDto);

        UserDTO result = userController.findByUsername(username);

        assertNotNull(result);
        assertEquals(foundUserDto.id(), result.id());
        assertEquals(username, result.username());

        verify(userDataSource).findByUsername(username);
    }
}