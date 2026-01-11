package com.fiap.auth_service.infrastructure.security;

import com.fiap.auth_service._webApi.data.persistence.repository.JpaUserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private JpaUserRepository repository;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        String username = "validUser";
        UserDetails mockUser = mock(UserDetails.class);
        
        when(repository.findByUsername(username)).thenReturn(mockUser);

        UserDetails result = authorizationService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(repository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        String username = "nonExistentUser";
        
        when(repository.findByUsername(username)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> 
            authorizationService.loadUserByUsername(username)
        );

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(repository, times(1)).findByUsername(username);
    }
}
