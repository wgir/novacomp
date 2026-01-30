package com.novacomp.notifications.api;

import com.novacomp.notifications.channel.email.EmailNotification;
import com.novacomp.notifications.channel.email.EmailProvider;
import com.novacomp.notifications.channel.email.EmailSender;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AsyncNotificationTest {

    @Test
    void sendAsync_ShouldCompleteSuccessfully() throws Exception {
        EmailProvider provider = mock(EmailProvider.class);
        when(provider.getProviderName()).thenReturn("TestProvider");
        when(provider.sendEmail(any())).thenReturn(true);

        NotificationChannel channel = new EmailSender(provider);
        EmailNotification notification = EmailNotification.builder()
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .build();

        CompletableFuture<NotificationResult> future = channel.sendAsync(notification);
        NotificationResult result = future.get(5, TimeUnit.SECONDS);

        assertTrue(result.success());
        assertEquals("TestProvider", result.providerName());
    }

    @Test
    void sendAsync_WithCustomExecutor_ShouldUseExecutor() throws Exception {
        EmailProvider provider = mock(EmailProvider.class);
        when(provider.getProviderName()).thenReturn("TestProvider");
        when(provider.sendEmail(any())).thenReturn(true);

        NotificationChannel channel = new EmailSender(provider);
        EmailNotification notification = EmailNotification.builder()
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .build();

        AtomicBoolean usedCustomExecutor = new AtomicBoolean(false);
        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            usedCustomExecutor.set(true);
            return new Thread(r);
        });

        try {
            CompletableFuture<NotificationResult> future = channel.sendAsync(notification, executor);
            NotificationResult result = future.get(5, TimeUnit.SECONDS);

            assertTrue(result.success());
            assertTrue(usedCustomExecutor.get(), "Custom executor should have been used");
        } finally {
            executor.shutdown();
        }
    }
}
