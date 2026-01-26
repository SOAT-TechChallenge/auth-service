package com.fiap.auth_service.core.gateways;

import com.fiap.auth_service.core.interfaces.NotificationDataSource;

public class NotificationGatewayImpl implements  NotificationGateway {

    private final NotificationDataSource notificationDataSource;


    public NotificationGatewayImpl(NotificationDataSource notificationDataSource) {
        this.notificationDataSource = notificationDataSource;
    }

    
    public void sendEmailNotification(String to, String subject, String body) {
        notificationDataSource.sendEmailNotification(to, subject, body);
    }
}
