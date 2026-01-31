package com.novacomp.notifications.channel.sms;

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
class SmsSenderTest {

        @Mock
        private SmsProvider provider;

        private SmsSender sender;

        @BeforeEach
        void setUp() {
                when(provider.getProviderName()).thenReturn("Twilio");
                sender = new SmsSender(provider);
        }

        @Test
        void send_ShouldReturnSuccess() throws Exception {
                // Arrange
                when(provider.sendSms(any(SmsNotification.class))).thenReturn(true);

                SmsNotification notification = SmsNotification.builder()
                                .phoneNumber("+1234567890")
                                .message("Hello")
                                .build();

                // Act
                NotificationResult result = sender.send(notification);

                // Assert
                assertTrue(result.success());
                assertEquals("SMS", result.channelName());
                assertEquals("Twilio", result.providerName());
                assertNotNull(result.messageId());

                verify(provider).sendSms(notification);
        }

        @Test
        void send_ShouldReturnFailure_WhenProviderReturnsFalse() throws Exception {
                // Arrange
                when(provider.sendSms(any(SmsNotification.class))).thenReturn(false);

                SmsNotification notification = SmsNotification.builder()
                                .phoneNumber("+1234567890")
                                .message("Hello")
                                .build();

                // Act
                NotificationResult result = sender.send(notification);

                // Assert
                assertFalse(result.success());
                assertEquals("SMS", result.channelName());
                assertEquals("Twilio", result.providerName());
                assertEquals("Provider returned failure.", result.message());
        }

        @Test
        void send_ShouldThrowException_WhenProviderThrows() throws Exception {
                // Arrange
                doThrow(new RuntimeException("Twilio API Error")).when(provider).sendSms(any());

                SmsNotification notification = SmsNotification.builder()
                                .phoneNumber("+1234567890")
                                .message("Hello")
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
                assertEquals("SMS", result.channelName());
                assertEquals("Twilio", result.providerName());
                assertTrue(result.message().contains("Invalid notification type"));
        }

        @Test
        void send_WithEmptySmsFields_ShouldReturnFailure() {
                // Arrange
                SmsNotification notification = SmsNotification.builder()
                                .phoneNumber("")
                                .message("")
                                .build();

                // Act
                NotificationResult result = sender.send(notification);

                // Assert
                assertFalse(result.success());
                assertEquals("SMS", result.channelName());
                assertTrue(result.message().contains("Validation failed"));
                assertTrue(result.message().contains("Phone number cannot be empty"));
        }
}
