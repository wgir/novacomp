package com.novacomp.notifications.channel.slack;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import com.novacomp.notifications.channel.email.EmailNotification;
import com.novacomp.notifications.provider.slack.SlackWebhookProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlackSenderTest {

    @Mock
    private SlackProvider provider;

    @InjectMocks
    private SlackSender sender;

    @Test
    void send_ShouldReturnSuccess_WhenProviderSucceeds() {
        // Arrange
        when(provider.getProviderName()).thenReturn("Slack");
        when(provider.sendSlackMessage(any(SlackNotification.class))).thenReturn(true);

        SlackNotification notification = SlackNotification.builder()
                .channel("#general")
                .text("Hello Slack!")
                .build();

        // Act
        NotificationResult result = sender.send(notification);

        // Assert
        assertTrue(result.success());
        assertEquals("SLACK", result.channelName());
        assertEquals("Slack", result.providerName());
        assertNotNull(result.messageId());
        verify(provider).sendSlackMessage(notification);
    }

    @Test
    void send_ShouldReturnFailure_WhenProviderReturnsFalse() {
        // Arrange
        when(provider.getProviderName()).thenReturn("Slack");
        when(provider.sendSlackMessage(any(SlackNotification.class))).thenReturn(false);

        SlackNotification notification = SlackNotification.builder()
                .channel("#test")
                .text("Failure test")
                .build();

        // Act
        NotificationResult result = sender.send(notification);

        // Assert
        assertFalse(result.success());
        assertEquals("Provider returned failure.", result.message());
    }

    @Test
    void send_ShouldThrowException_WhenProviderThrows() {
        // Arrange
        when(provider.getProviderName()).thenReturn("Slack");
        doThrow(new RuntimeException("Slack API Error")).when(provider).sendSlackMessage(any());

        SlackNotification notification = SlackNotification.builder()
                .channel("#error")
                .text("Exception test")
                .build();

        // Act & Assert
        NotificationException exception = assertThrows(NotificationException.class,
                () -> sender.send(notification));
        assertTrue(exception.getMessage().contains("Failed to send Slack message via Slack"));
    }

    @Test
    void send_WithInvalidNotificationType_ShouldReturnFailure() {
        // Arrange
        when(provider.getProviderName()).thenReturn("Slack");

        EmailNotification emailNotification = EmailNotification.builder()
                .to("test@example.com")
                .subject("Wrong type")
                .body("This should fail")
                .build();

        // Act
        NotificationResult result = sender.send(emailNotification);

        // Assert
        assertFalse(result.success());
        assertEquals("SLACK", result.channelName());
        assertTrue(result.message().contains("Invalid notification type"));
    }

    @Test
    void slackWebhookProvider_WithNullWebhookUrl_ShouldThrowException() {
        // Arrange
        SlackWebhookProvider webhookProvider = new SlackWebhookProvider(null);
        SlackSender slackSender = new SlackSender(webhookProvider);

        SlackNotification notification = SlackNotification.builder()
                .channel("#general")
                .text("Test")
                .build();

        // Act & Assert
        NotificationException exception = assertThrows(NotificationException.class,
                () -> slackSender.send(notification));
        assertEquals("Slack webhook URL is missing", exception.getCause().getMessage());
    }

    @Test
    void slackWebhookProvider_WithEmptyWebhookUrl_ShouldThrowException() {
        // Arrange
        SlackWebhookProvider webhookProvider = new SlackWebhookProvider("");
        SlackSender slackSender = new SlackSender(webhookProvider);

        SlackNotification notification = SlackNotification.builder()
                .channel("#general")
                .text("Test")
                .build();

        // Act & Assert
        NotificationException exception = assertThrows(NotificationException.class,
                () -> slackSender.send(notification));
        assertEquals("Slack webhook URL is missing", exception.getCause().getMessage());
    }
}
