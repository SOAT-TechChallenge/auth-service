package com.fiap.auth_service.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fiap.auth_service._webApi.data.persistence.entity.UserEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fiap.auth_service.infrastructure.security.TokenService;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private UserEntity userEntity;

    private final String secret = "mySuperSecretKey123";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", secret);
    }

    @Test
    void generateToken_ShouldReturnToken_WhenUserIsValid() {
        when(userEntity.getUsername()).thenReturn("testuser");

        String token = tokenService.generateToken(userEntity);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken_ShouldReturnSubject_WhenTokenIsValid() {
        when(userEntity.getUsername()).thenReturn("testuser");
        String token = tokenService.generateToken(userEntity);

        String subject = tokenService.validateToken(token);

        assertEquals("testuser", subject);
    }

    @Test
    void validateToken_ShouldReturnEmptyString_WhenTokenIsInvalid() {
        String invalidToken = "invalid.header.payload";

        String subject = tokenService.validateToken(invalidToken);

        assertEquals("", subject);
    }

    @Test
    void validateToken_ShouldReturnEmptyString_WhenTokenIsSignedWithDifferentSecret() {
        Algorithm algorithm = Algorithm.HMAC256("wrongSecret");
        String forgedToken = JWT.create()
                .withIssuer("auth-api")
                .withSubject("testuser")
                .sign(algorithm);

        String subject = tokenService.validateToken(forgedToken);

        assertEquals("", subject);
    }
}