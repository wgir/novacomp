package com.novacomp.notifications.channel.email;

import com.novacomp.notifications.api.Notification;
import com.novacomp.notifications.api.NotificationChannel;
import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of NotificationChannel for Emails.
 * Delegates the actual sending to a configured EmailProvider.
 */
@Slf4j
@RequiredArgsConstructor
public class EmailSender implements NotificationChannel {

    private final EmailProvider provider;

    @Override
    public NotificationResult send(Notification notification) {
        if (!(notification instanceof EmailNotification emailNotification)) {
            return NotificationResult.failure("EMAIL", provider.getProviderName(),
                    "Invalid notification type. Expected EmailNotification.");
        }

        var validation = emailNotification.validate();
        if (!validation.isValid()) {
            return NotificationResult.failure("EMAIL", provider.getProviderName(),
                    "Validation failed: " + String.join(", ", validation.errors()));
        }

        try {
            log.info("Sending email to {} via {}", emailNotification.getTo(), provider.getProviderName());
            boolean sent = provider.sendEmail(emailNotification);

            if (sent) {
                return NotificationResult.success("EMAIL", provider.getProviderName(), "generated-message-id");
            } else {
                return NotificationResult.failure("EMAIL", provider.getProviderName(), "Provider returned failure.");
            }
        } catch (Exception e) {
            log.error("Failed to send email", e);
            throw new NotificationException("Failed to send email via " + provider.getProviderName(), e);
        }
    }
}
