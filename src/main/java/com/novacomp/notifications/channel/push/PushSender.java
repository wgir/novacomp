package com.novacomp.notifications.channel.push;

import com.novacomp.notifications.api.Notification;
import com.novacomp.notifications.api.NotificationChannel;
import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of NotificationChannel for Push Notifications.
 * Delegates the actual sending to a configured PushProvider.
 */
@Slf4j
@RequiredArgsConstructor
public class PushSender implements NotificationChannel {

    private final PushProvider provider;

    @Override
    public NotificationResult send(Notification notification) {
        if (!(notification instanceof PushNotification pushNotification)) {
            return NotificationResult.failure("PUSH", provider.getProviderName(),
                    "Invalid notification type. Expected PushNotification.");
        }

        try {
            log.info("Sending Push to {} via {}", pushNotification.getToken(), provider.getProviderName());
            boolean sent = provider.sendPush(pushNotification);

            if (sent) {
                return NotificationResult.success("PUSH", provider.getProviderName(), "generated-push-id");
            } else {
                return NotificationResult.failure("PUSH", provider.getProviderName(), "Provider returned failure.");
            }
        } catch (Exception e) {
            log.error("Failed to send Push", e);
            throw new NotificationException("Failed to send Push via " + provider.getProviderName(), e);
        }
    }
}
