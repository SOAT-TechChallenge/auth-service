package com.fiap.auth_service._webApi.data.persistence.repository;

import com.fiap.auth_service._webApi.data.persistence.entity.UserEntity;
import com.fiap.auth_service.core.application.dto.UserDTO;
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
class UserDataSourceImplTest {

    @Mock
    private JpaUserRepository jpaRepository;

    @InjectMocks
    private UserDataSourceImpl userDataSource;

    @Test
    void save_ShouldSaveAndReturnUserDTO() {
        UserDTO inputDto = new UserDTO(UUID.randomUUID(), "testuser","123456", "test@email.com");
        UserEntity savedEntity = new UserEntity(UUID.randomUUID(), "testuser", "123456", "test@email.com");

        when(jpaRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        UserDTO result = userDataSource.save(inputDto);

        assertNotNull(result);
        assertEquals(savedEntity.getUsername(), result.username());
        assertEquals(savedEntity.getEmail(), result.email());
        verify(jpaRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUserDTO() {
        String username = "testuser";
        UserEntity entity = new UserEntity(UUID.randomUUID(), username, "123456", "test@email.com");

        when(jpaRepository.findFirstByUsername(username)).thenReturn(entity);

        UserDTO result = userDataSource.findByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.username());
        verify(jpaRepository, times(1)).findFirstByUsername(username);
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldReturnNull() {
        String username = "nonexistent";

        when(jpaRepository.findFirstByUsername(username)).thenReturn(null);

        UserDTO result = userDataSource.findByUsername(username);

        assertNull(result);
        verify(jpaRepository, times(1)).findFirstByUsername(username);
    }
}