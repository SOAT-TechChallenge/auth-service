package com.fiap.auth_service._webApi.mappers;

import com.fiap.auth_service._webApi.data.persistence.entity.UserEntity;
import com.fiap.auth_service.core.application.dto.UserDTO;

public class UserMapper {

    public static UserDTO toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UserDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail()
        );
    }

    public static UserEntity toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        return new UserEntity(dto.id(), dto.username(), dto.password(), dto.email());
    }

}
