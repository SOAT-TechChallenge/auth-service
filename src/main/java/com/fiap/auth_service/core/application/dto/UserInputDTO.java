package com.fiap.auth_service.core.application.dto;

public record UserInputDTO (
    String username,
    String password,
    String email
){}
