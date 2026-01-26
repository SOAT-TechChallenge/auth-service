package com.fiap.auth_service.core.gateways;

public interface NotificationGateway {

    public void sendEmailNotification(String to, String subject, String body);
}
