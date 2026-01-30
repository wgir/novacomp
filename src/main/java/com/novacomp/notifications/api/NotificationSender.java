package com.novacomp.notifications.api;

/**
 * Functional interface for sending notifications.
 */
@FunctionalInterface
public interface NotificationSender {
    NotificationResult send(Notification notification);
}
