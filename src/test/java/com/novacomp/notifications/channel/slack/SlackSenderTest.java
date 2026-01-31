package com.novacomp.notifications.channel.slack;

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
class SlackSenderTest {

    @Mock
    private SlackProvider provider;

    private SlackSender sender;

    @BeforeEach
    void setUp() {
        lenient().when(provider.getProviderName()).thenReturn("SlackProvider");
        sender = new SlackSender(provider);
    }

    @Test
    void send_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(provider.sendSlackMessage(any(SlackNotification.class))).thenReturn(true);

        SlackNotification notification = SlackNotification.builder()
                .channel("#general")
                .text("Hello Slack")
                .build();

        // Act
        NotificationResult result = sender.send(notification);

        // Assert
        assertTrue(result.success());
        assertEquals("SLACK", result.channelName());
        assertEquals("SlackProvider", result.providerName());
        assertNotNull(result.messageId());

        verify(provider).sendSlackMessage(notification);
    }

    @Test
    void send_ShouldReturnFailure_WhenProviderReturnsFalse() throws Exception {
        // Arrange
        when(provider.sendSlackMessage(any(SlackNotification.class))).thenReturn(false);

        SlackNotification notification = SlackNotification.builder()
                .channel("#general")
                .text("Hello")
                .build();

        // Act
        NotificationResult result = sender.send(notification);

        // Assert
        assertFalse(result.success());
        assertEquals("SLACK", result.channelName());
        assertEquals("SlackProvider", result.providerName());
        assertEquals("Provider returned failure.", result.message());
    }

    @Test
    void send_ShouldThrowException_WhenProviderThrows() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Slack API Error")).when(provider).sendSlackMessage(any());

        SlackNotification notification = SlackNotification.builder()
                .channel("#general")
                .text("Hello")
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
        assertEquals("SLACK", result.channelName());
        assertEquals("SlackProvider", result.providerName());
        assertTrue(result.message().contains("Invalid notification type"));
    }

    @Test
    void send_WithEmptyFields_ShouldReturnFailure() {
        // Arrange
        SlackNotification notification = SlackNotification.builder()
                .channel("")
                .text("")
                .build();

        // Act
        NotificationResult result = sender.send(notification);

        // Assert
        assertFalse(result.success());
        assertEquals("SLACK", result.channelName());
        assertTrue(result.message().contains("Validation failed"));
    }
}
