package com.fiap.auth_service._webApi.data.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.fiap.auth_service._webApi.data.persistence.entity.UserEntity;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findFirstByUsername(String username);

    UserDetails findByUsername(String username);
}
