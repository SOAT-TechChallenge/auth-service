package com.fiap.auth_service._webApi.data.persistence.external;

import com.fiap.auth_service.core.interfaces.NotificationDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationDataSourceImpl implements NotificationDataSource {

    private static final Logger log = LoggerFactory.getLogger(NotificationDataSourceImpl.class);

    private final RestTemplate restTemplate;
    private final String notificationUrl;
    private final String internalToken;

    public NotificationDataSourceImpl(
            RestTemplate restTemplate,
            @Value("${app.notification.url}") String notificationUrl,
            @Value("${app.security.internal-token}") String internalToken) {
        this.restTemplate = restTemplate;
        this.notificationUrl = notificationUrl;
        this.internalToken = internalToken;
    }

    @Override
    public void sendEmailNotification(String to, String subject, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-apigateway-token", internalToken);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("to", to);
            requestBody.put("subject", subject);
            requestBody.put("body", body);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            String targetEndpoint = notificationUrl + "/api/notification/send-email";

            restTemplate.postForObject(targetEndpoint, requestEntity, Void.class);

        } catch (Exception e) {
            log.error("ALERTA: Falha ao comunicar com Notification Service. Detalhe: {}", e.getMessage());
        }
    }
}