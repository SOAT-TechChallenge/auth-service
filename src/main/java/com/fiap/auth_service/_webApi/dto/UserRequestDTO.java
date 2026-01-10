package com.fiap.auth_service._webApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank(message = "Username é obrigatório")
        String username,
        @NotBlank(message = "Password é obrigatório")
        String password,
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email em formato inválido")
        String email
        ) {

}
