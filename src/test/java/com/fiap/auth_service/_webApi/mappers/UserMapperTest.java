package com.fiap.auth_service._webApi.mappers;

import com.fiap.auth_service._webApi.data.persistence.entity.UserEntity;
import com.fiap.auth_service.core.application.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDto_ShouldReturnDto_WhenEntityIsValid() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity(id, "testuser", "123456", "test@email.com");

        UserDTO result = UserMapper.toDto(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getUsername(), result.username());
        assertEquals(entity.getPassword(), result.password());
        assertEquals(entity.getEmail(), result.email());
    }

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        UserDTO result = UserMapper.toDto(null);

        assertNull(result);
    }

    @Test
    void toEntity_ShouldReturnEntity_WhenDtoIsValid() {
        UUID id = UUID.randomUUID();
        UserDTO dto = new UserDTO(id, "testuser", "123456", "test@email.com");

        UserEntity result = UserMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(dto.username(), result.getUsername());
        assertEquals(dto.password(), result.getPassword());
        assertEquals(dto.email(), result.getEmail());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        UserEntity result = UserMapper.toEntity(null);

        assertNull(result);
    }
}