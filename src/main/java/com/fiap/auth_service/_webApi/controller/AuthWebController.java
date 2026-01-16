package com.fiap.auth_service._webApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.auth_service._webApi.data.persistence.entity.UserEntity;
import com.fiap.auth_service._webApi.dto.LoginRequestDTO;
import com.fiap.auth_service._webApi.dto.LoginResponseDTO;
import com.fiap.auth_service.core.application.dto.UserDTO;
import com.fiap.auth_service.core.controler.AuthController;
import com.fiap.auth_service.core.interfaces.UserDataSource;
import com.fiap.auth_service.infrastructure.security.TokenService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "APIs relacionadas à Autenticação do projeto TechChallenge")
public class AuthWebController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    private final AuthController authController;

    public AuthWebController(UserDataSource userDataSource) {
        this.authController = AuthController.build(userDataSource);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @GetMapping("/validate")
    public ResponseEntity<UserDTO> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.startsWith("Bearer ") 
            ? tokenHeader.replace("Bearer ", "") 
            : tokenHeader;
        
        try {
            String username = tokenService.validateToken(token); 
            return ResponseEntity.ok(authController.validateByUsername(username));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
