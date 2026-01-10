package com.fiap.auth_service._webApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.auth_service._webApi.dto.UserRequestDTO;
import com.fiap.auth_service.core.application.dto.UserDTO;
import com.fiap.auth_service.core.application.dto.UserInputDTO;
import com.fiap.auth_service.core.controler.UserController;
import com.fiap.auth_service.core.interfaces.UserDataSource;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "APIs relacionadas aos Usuários")
public class UserWebController {

    private final UserController controller;

    public UserWebController(UserDataSource userDataSource, PasswordEncoder passwordEncoder) {
        this.controller = UserController.build(userDataSource, passwordEncoder);
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserRequestDTO request) {
        var result = controller.create(
                new UserInputDTO(
                        request.username(),
                        request.password(),
                        request.email()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/find-by-username/{username}")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username) {
        var result = controller.findByUsername(username);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
