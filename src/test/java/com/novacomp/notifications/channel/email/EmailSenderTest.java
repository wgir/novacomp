package com.novacomp.notifications.channel.email;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {

    @Mock
    private EmailProvider provider;

    @InjectMocks
    private EmailSender sender;

    @Test
    void send_ShouldReturnSuccess_WhenProviderSucceeds() throws Exception {
        // Arrange
        when(provider.getProviderName()).thenReturn("TestProvider");
        when(provider.sendEmail(any(EmailNotification.class))).thenReturn(true);

        EmailNotification notification = EmailNotification.builder()
                .to("test@example.com")
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
        when(provider.getProviderName()).thenReturn("TestProvider");
        when(provider.sendEmail(any(EmailNotification.class))).thenReturn(false);

        EmailNotification notification = EmailNotification.builder()
                .to("test@example.com")
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
        when(provider.getProviderName()).thenReturn("TestProvider");
        doThrow(new RuntimeException("API Error")).when(provider).sendEmail(any());

        EmailNotification notification = EmailNotification.builder()
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .build();

        // Act & Assert
        assertThrows(NotificationException.class, () -> sender.send(notification));
    }
}
