package com.fiap.auth_service._webApi.data.persistence.external;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDataSourceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private NotificationDataSourceImpl notificationDataSource;

    // Valores simulados para as variáveis @Value
    private final String FAKE_URL = "http://fake-notification-service";
    private final String FAKE_TOKEN = "segredo-123";

    // Captor para inspecionar o que foi enviado dentro do HttpEntity
    @Captor
    private ArgumentCaptor<HttpEntity<Map<String, String>>> httpEntityCaptor;

    @BeforeEach
    void setUp() {
        notificationDataSource = new NotificationDataSourceImpl(restTemplate, FAKE_URL, FAKE_TOKEN);
    }

    @Test
    @DisplayName("Deve enviar notificação com URL, Headers e Body corretos")
    void shouldSendEmailNotificationSuccessfully() {
        // Arrange
        String to = "usuario@teste.com";
        String subject = "Bem-vindo";
        String body = "Olá, seja bem-vindo!";
        String expectedEndpoint = FAKE_URL + "/api/notification/send-email";

        // Act
        notificationDataSource.sendEmailNotification(to, subject, body);

        // Assert
        verify(restTemplate, times(1))
            .postForObject(eq(expectedEndpoint), httpEntityCaptor.capture(), eq(Void.class));

        HttpEntity<Map<String, String>> capturedEntity = httpEntityCaptor.getValue();
        HttpHeaders headers = capturedEntity.getHeaders();
        Map<String, String> requestBody = capturedEntity.getBody();

        assertNotNull(headers);
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals(FAKE_TOKEN, headers.getFirst("x-apigateway-token"));

        assertNotNull(requestBody);
        assertEquals(to, requestBody.get("to"));
        assertEquals(subject, requestBody.get("subject"));
        assertEquals(body, requestBody.get("body"));
    }

    @Test
    @DisplayName("Deve capturar exceção e não quebrar a aplicação quando o serviço externo falhar")
    void shouldHandleExceptionGracefully() {
        // Arrange
        String expectedEndpoint = FAKE_URL + "/api/notification/send-email";

        // Simula um erro de conexão (ex: Notification Service fora do ar)
        when(restTemplate.postForObject(eq(expectedEndpoint), any(), eq(Void.class)))
                .thenThrow(new RuntimeException("Connection Timeout"));

        // Act
        notificationDataSource.sendEmailNotification("to", "sub", "body");

        // Assert
        verify(restTemplate, times(1)).postForObject(eq(expectedEndpoint), any(), eq(Void.class));
    }
}