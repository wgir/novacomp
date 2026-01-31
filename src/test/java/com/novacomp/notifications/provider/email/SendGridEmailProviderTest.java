package com.novacomp.notifications.provider.email;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.channel.email.EmailNotification;
import com.novacomp.notifications.channel.email.EmailSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SendGridEmailProviderTest {

    @Test
    void sendGridProvider_WithNullApiKey_ShouldThrowException() {
        SendGridEmailProvider sendGridProvider = new SendGridEmailProvider(null);
        EmailSender emailSender = new EmailSender(sendGridProvider);

        EmailNotification notification = EmailNotification.builder()
                .to("test@example.com")
                .from("sender@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        NotificationException exception = assertThrows(
                NotificationException.class,
                () -> emailSender.send(notification));

        assertEquals("SendGrid API Key is missing", exception.getCause().getMessage());
    }

    @Test
    void sendGridProvider_WithEmptyApiKey_ShouldThrowException() {
        SendGridEmailProvider sendGridProvider = new SendGridEmailProvider("");
        EmailSender emailSender = new EmailSender(sendGridProvider);

        EmailNotification notification = EmailNotification.builder()
                .to("test@example.com")
                .from("sender@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        NotificationException exception = assertThrows(
                NotificationException.class,
                () -> emailSender.send(notification));

        assertEquals("SendGrid API Key is missing", exception.getCause().getMessage());
    }
}
