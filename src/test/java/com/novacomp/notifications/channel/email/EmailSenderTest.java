package com.novacomp.notifications.channel.email;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import com.novacomp.notifications.channel.sms.SmsNotification;
import com.novacomp.notifications.provider.email.SendGridEmailProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {

        @Mock
        private EmailProvider provider;

        private EmailSender sender;

        @BeforeEach
        void setUp() {
                // Use lenient() because some specialized tests (like SendGrid ones)
                // create their own sender and don't use this mock.
                lenient().when(provider.getProviderName()).thenReturn("TestProvider");
                sender = new EmailSender(provider);
        }

        @Test
        void send_ShouldReturnSuccess_WhenProviderSucceeds() throws Exception {
                // Arrange
                when(provider.sendEmail(any(EmailNotification.class))).thenReturn(true);

                EmailNotification notification = EmailNotification.builder()
                                .to("test@example.com")
                                .from("sender@example.com")
                                .subject("Test")
                                .body("Body")
                                .build();

                // Act
                NotificationResult result = sender.send(notification);

                // Assert
                assertTrue(result.success());
                assertEquals("EMAIL", result.channelName());
                assertEquals("TestProvider", result.providerName());
                assertNotNull(result.messageId());

                verify(provider).sendEmail(notification);
        }

        @Test
        void send_ShouldReturnFailure_WhenProviderReturnsFalse() throws Exception {
                // Arrange
                when(provider.sendEmail(any(EmailNotification.class))).thenReturn(false);

                EmailNotification notification = EmailNotification.builder()
                                .to("test@example.com")
                                .from("sender@example.com")
                                .subject("Test")
                                .body("Body")
                                .build();

                // Act
                NotificationResult result = sender.send(notification);

                // Assert
                assertFalse(result.success());
                assertEquals("Provider returned failure.", result.message());
        }

        @Test
        void send_ShouldThrowException_WhenProviderThrows() throws Exception {
                // Arrange
                doThrow(new RuntimeException("API Error")).when(provider).sendEmail(any());

                EmailNotification notification = EmailNotification.builder()
                                .to("test@example.com")
                                .from("sender@example.com")
                                .subject("Test")
                                .body("Body")
                                .build();

                // Act & Assert
                assertThrows(NotificationException.class, () -> sender.send(notification));
        }

        @Test
        void send_WithInvalidNotificationType_ShouldReturnFailure() {
                SmsNotification smsNotification = SmsNotification.builder()
                                .phoneNumber("+1234567890")
                                .message("Test message")
                                .build();

                NotificationResult result = sender.send(smsNotification);

                assertFalse(result.success());
                assertEquals("EMAIL", result.channelName());
                // Since provider is mocked to return "TestProvider" in setUp
                assertEquals("TestProvider", result.providerName());
                assertTrue(result.message().contains("Invalid notification type"));
        }

        @Test
        void send_WithInvalidEmail_ShouldReturnFailure() {
                EmailNotification notification = EmailNotification.builder()
                                .to("invalid-email") // Missing @
                                .from("sender@example.com")
                                .subject("Test")
                                .body("Test body")
                                .build();

                NotificationResult result = sender.send(notification);

                assertFalse(result.success());
                assertEquals("EMAIL", result.channelName());
                assertTrue(result.message().contains("Validation failed"));
                assertTrue(result.message().contains("Invalid recipient email"));
        }

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
