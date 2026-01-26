package com.fiap.auth_service.core.interfaces;

public interface NotificationDataSource {

    public void sendEmailNotification(String to, String subject, String body);
}
