package com.fiap.auth_service.core.application.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record UserDTO(
    UUID id,
    String username,
    String password,
    String email
) {}
