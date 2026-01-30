package com.novacomp.notifications.channel.push;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import com.novacomp.notifications.channel.email.EmailNotification;
import com.novacomp.notifications.provider.push.FirebasePushProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PushSenderTest {

    @Mock
    private PushProvider provider;

    @InjectMocks
    private PushSender sender;

    @Test
    void send_ShouldReturnSuccess() throws Exception {
        when(provider.getProviderName()).thenReturn("FCM");
        when(provider.sendPush(any(PushNotification.class))).thenReturn(true);

        PushNotification notification = PushNotification.builder()
                .token("device_token")
                .title("Title")
                .body("Body")
                .build();

        NotificationResult result = sender.send(notification);

        assertTrue(result.success());
    }

    @Test
    void firebaseProvider_WithNullProjectId_ShouldThrowException() {
        FirebasePushProvider firebaseProvider = new FirebasePushProvider(null, "path/to/key.json");
        PushSender pushSender = new PushSender(firebaseProvider);

        PushNotification notification = PushNotification.builder()
                .token("device_token")
                .title("Title")
                .body("Body")
                .build();

        NotificationException exception = assertThrows(
                NotificationException.class,
                () -> pushSender.send(notification));

        assertEquals("Firebase credentials are missing", exception.getCause().getMessage());
    }

    @Test
    void firebaseProvider_WithNullServiceAccountKeyPath_ShouldThrowException() {
        FirebasePushProvider firebaseProvider = new FirebasePushProvider("test-project-id", null);
        PushSender pushSender = new PushSender(firebaseProvider);

        PushNotification notification = PushNotification.builder()
                .token("device_token")
                .title("Title")
                .body("Body")
                .build();

        NotificationException exception = assertThrows(
                NotificationException.class,
                () -> pushSender.send(notification));

        assertEquals("Firebase credentials are missing", exception.getCause().getMessage());
    }

    @Test
    void send_WithInvalidNotificationType_ShouldReturnFailure() {
        when(provider.getProviderName()).thenReturn("FCM");

        EmailNotification emailNotification = EmailNotification.builder()
                .to("test@example.com")
                .subject("Test")
                .body("Test body")
                .build();

        NotificationResult result = sender.send(emailNotification);

        assertFalse(result.success());
        assertEquals("PUSH", result.channelName());
        assertEquals("FCM", result.providerName());
        assertTrue(result.message().contains("Invalid notification type"));
    }

    @Test
    void send_WhenProviderReturnsFalse_ShouldReturnFailure() throws Exception {
        when(provider.getProviderName()).thenReturn("FCM");
        when(provider.sendPush(any(PushNotification.class))).thenReturn(false);

        PushNotification notification = PushNotification.builder()
                .token("device_token")
                .title("Title")
                .body("Body")
                .build();

        NotificationResult result = sender.send(notification);

        assertFalse(result.success());
        assertEquals("PUSH", result.channelName());
        assertEquals("FCM", result.providerName());
        assertEquals("Provider returned failure.", result.message());
    }
}
