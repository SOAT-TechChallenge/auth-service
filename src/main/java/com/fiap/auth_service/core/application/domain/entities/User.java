package com.fiap.auth_service.core.application.domain.entities;

import java.util.UUID;

public class User {

    private UUID id;
    private String username;
    private String password;
    private String email;

    private User(UUID id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public static User build(UUID id, String username, String password, String email) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username é obrigatório");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password é obrigatório");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        return new User(id, username, password, email);
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
