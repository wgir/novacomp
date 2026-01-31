package com.novacomp.notifications.channel.push;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import com.novacomp.notifications.channel.email.EmailNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PushSenderTest {

        @Mock
        private PushProvider provider;

        private PushSender sender;

        @BeforeEach
        void setUp() {
                when(provider.getProviderName()).thenReturn("FCM");
                sender = new PushSender(provider);
        }

        @Test
        void send_ShouldReturnSuccess() throws Exception {
                // Arrange
                when(provider.sendPush(any(PushNotification.class))).thenReturn(true);

                PushNotification notification = PushNotification.builder()
                                .token("device_token")
                                .title("Title")
                                .body("Body")
                                .build();

                // Act
                NotificationResult result = sender.send(notification);

                // Assert
                assertTrue(result.success());
                assertEquals("PUSH", result.channelName());
                assertEquals("FCM", result.providerName());
                assertNotNull(result.messageId());

                verify(provider).sendPush(notification);
        }

        @Test
        void send_ShouldReturnFailure_WhenProviderReturnsFalse() throws Exception {
                // Arrange
                when(provider.sendPush(any(PushNotification.class))).thenReturn(false);

                PushNotification notification = PushNotification.builder()
                                .token("device_token")
                                .title("Title")
                                .body("Body")
                                .build();

                // Act
                NotificationResult result = sender.send(notification);

                // Assert
                assertFalse(result.success());
                assertEquals("PUSH", result.channelName());
                assertEquals("FCM", result.providerName());
                assertEquals("Provider returned failure.", result.message());
        }

        @Test
        void send_ShouldThrowException_WhenProviderThrows() throws Exception {
                // Arrange
                doThrow(new RuntimeException("Firebase API Error")).when(provider).sendPush(any());

                PushNotification notification = PushNotification.builder()
                                .token("device_token")
                                .title("Title")
                                .body("Body")
                                .build();

                // Act & Assert
                assertThrows(NotificationException.class, () -> sender.send(notification));
        }

        @Test
        void send_WithInvalidNotificationType_ShouldReturnFailure() {
                // Arrange
                EmailNotification emailNotification = EmailNotification.builder()
                                .to("test@example.com")
                                .from("sender@example.com")
                                .subject("Test")
                                .body("Test body")
                                .build();

                // Act
                NotificationResult result = sender.send(emailNotification);

                // Assert
                assertFalse(result.success());
                assertEquals("PUSH", result.channelName());
                assertEquals("FCM", result.providerName());
                assertTrue(result.message().contains("Invalid notification type"));
        }

        @Test
        void send_WithEmptyPushToken_ShouldReturnFailure() {
                // Arrange
                PushNotification notification = PushNotification.builder()
                                .token("")
                                .title("Title")
                                .body("Body")
                                .build();

                // Act
                NotificationResult result = sender.send(notification);

                // Assert
                assertFalse(result.success());
                assertEquals("PUSH", result.channelName());
                assertTrue(result.message().contains("Validation failed"));
                assertTrue(result.message().contains("Push token cannot be empty"));
        }
}
