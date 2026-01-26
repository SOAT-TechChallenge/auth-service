package com.fiap.auth_service;

import com.fiap.auth_service._webApi.data.persistence.repository.JpaUserRepository;
import com.fiap.auth_service.core.interfaces.UserDataSource;
import com.fiap.auth_service.infrastructure.security.TokenService;
// Adicione a interface de notificação
import com.fiap.auth_service.core.interfaces.NotificationDataSource; 
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate; // Importante se não tiver o bean

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    // --- Configurações de Banco ---
    "api.security.token.secret=segredo-de-teste-muito-seguro-123",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    
    // --- NOVAS CONFIGURAÇÕES (Necessárias para o NotificationDataSourceImpl) ---
    "app.notification.url=http://localhost-teste",
    "app.security.internal-token=token-teste"
})
class AuthServiceApplicationTests {

    @MockBean
    private JpaUserRepository jpaUserRepository;

    @MockBean
    private UserDataSource userDataSource;

    @MockBean
    private TokenService tokenService;

    // Mockamos o NotificationDataSource para ele não tentar criar o RestTemplate real
    // ou tentar conectar na rede durante o teste de carga de contexto.
    @MockBean
    private NotificationDataSource notificationDataSource;

    // Se sua aplicação não tiver um @Bean de RestTemplate declarado na main class,
    // descomente a linha abaixo para evitar erro de "No qualifying bean of type RestTemplate":
    // @MockBean
    // private RestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

}