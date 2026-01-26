package com.fiap.auth_service.core.application.services;

import com.fiap.auth_service.core.gateways.NotificationGateway;

public class NotificationService {

    private final NotificationGateway notificationGateway;


    public NotificationService(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }


    public void sendEmail(String to, String subject, String body) {
        notificationGateway.sendEmailNotification(to, subject, body);
    }
}
