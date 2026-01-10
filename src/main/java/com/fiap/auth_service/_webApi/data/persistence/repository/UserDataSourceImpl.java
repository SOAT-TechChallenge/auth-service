package com.fiap.auth_service._webApi.data.persistence.repository;

import org.springframework.stereotype.Component;

import com.fiap.auth_service._webApi.mappers.UserMapper;
import com.fiap.auth_service.core.application.dto.UserDTO;
import com.fiap.auth_service.core.interfaces.UserDataSource;

@Component
public class UserDataSourceImpl implements UserDataSource {
    
    private final JpaUserRepository jpaRepository;

    public UserDataSourceImpl(JpaUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserDTO save(UserDTO userDomain) {
        var entity = UserMapper.toEntity(userDomain);
        entity = jpaRepository.save(entity);
    
        return UserMapper.toDto(entity);
    }

    @Override
    public UserDTO findByUsername(String username) {
        return UserMapper.toDto(jpaRepository.findFirstByUsername(username));
    }
}