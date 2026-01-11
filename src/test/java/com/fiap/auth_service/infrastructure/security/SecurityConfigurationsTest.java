package com.fiap.auth_service.infrastructure.security;

import com.fiap.auth_service._webApi.data.persistence.repository.JpaUserRepository;
import com.fiap.auth_service.core.interfaces.UserDataSource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityConfigurationsTest.MockControllerConfig.TestController.class) // <--- RESTRINGE O ESCOPO
@Import(SecurityConfigurations.class)
@TestPropertySource(properties = {
    "api.security.token.secret=segredo-de-teste-muito-seguro-123"
})
class SecurityConfigurationsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @MockBean
    private SecurityFilter securityFilter;

    // Mocks necessários para o contexto não falhar
    @MockBean
    private TokenService tokenService;

    @MockBean
    private JpaUserRepository userRepository;
    
    // Adicione este mock também, pois pode ser exigido por algum AutoConfiguration
    @MockBean
    private UserDataSource userDataSource;

    @TestConfiguration
    static class MockControllerConfig {
        @RestController
        static class TestController {
            @PostMapping("/auth/login") public void login() {}
            @PostMapping("/user/create") public void create() {}
        }
    }

    @BeforeEach
    void setup() throws Exception {
        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(securityFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
    }

    // ... Seus testes (beans_ShouldBeLoadedCorrectly, shouldAllowPublicEndpoints, etc) continuam iguais ...
    @Test
    void beans_ShouldBeLoadedCorrectly() {
        assertNotNull(authenticationManager);
        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    void shouldAllowPublicEndpoints() throws Exception {
        mockMvc.perform(post("/auth/login"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/create"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowSwaggerEndpoints() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldBlockOtherEndpoints() throws Exception {
        mockMvc.perform(get("/any/other/route"))
                .andExpect(status().isForbidden());
    }
}