package com.fiap.auth_service.core.gateways;

import com.fiap.auth_service.core.application.domain.entities.User;
import com.fiap.auth_service.core.application.dto.UserDTO;
import com.fiap.auth_service.core.interfaces.UserDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGatewayImplTest {

    @Mock
    private UserDataSource userDataSource;

    @InjectMocks
    private UserGatewayImpl userGateway;

    @Test
    void save_ShouldConvertAndSaveUser() {
        UUID id = UUID.randomUUID();
        User domainUser = User.build(id, "testuser", "pass123", "test@email.com");
        UserDTO returnedDto = new UserDTO(id, "testuser", "pass123", "test@email.com");

        when(userDataSource.save(any(UserDTO.class))).thenReturn(returnedDto);

        User result = userGateway.save(domainUser);

        assertNotNull(result);
        assertEquals(domainUser.getId(), result.getId());
        assertEquals(domainUser.getUsername(), result.getUsername());
        verify(userDataSource, times(1)).save(any(UserDTO.class));
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        String username = "testuser";
        UUID id = UUID.randomUUID();
        UserDTO foundDto = new UserDTO(id, username, "pass123", "test@email.com");

        when(userDataSource.findByUsername(username)).thenReturn(foundDto);

        User result = userGateway.findByUsername(username);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals("test@email.com", result.getEmail());
        verify(userDataSource, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_ShouldReturnNull_WhenNotExists() {
        String username = "nonexistent";

        when(userDataSource.findByUsername(username)).thenReturn(null);

        User result = userGateway.findByUsername(username);

        assertNull(result);
        verify(userDataSource, times(1)).findByUsername(username);
    }
}