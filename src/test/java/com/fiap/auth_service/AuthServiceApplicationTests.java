package com.fiap.auth_service;

import com.fiap.auth_service._webApi.data.persistence.repository.JpaUserRepository;
import com.fiap.auth_service.core.interfaces.UserDataSource;
import com.fiap.auth_service.infrastructure.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "api.security.token.secret=segredo-de-teste-muito-seguro-123",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AuthServiceApplicationTests {

    @MockBean
    private JpaUserRepository jpaUserRepository;

    @MockBean
    private UserDataSource userDataSource;

    @MockBean
    private TokenService tokenService;

	@Test
	void contextLoads() {
	}

}