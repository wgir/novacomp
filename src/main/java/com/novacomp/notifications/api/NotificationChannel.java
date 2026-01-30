package com.novacomp.notifications.api;

/**
 * Defines the contract for a notification channel (Email, SMS, Push).
 * A channel takes a generic Notification and returns a NotificationResult.
 */
public interface NotificationChannel {
    /**
     * Sends a notification through this channel.
     *
     * @param notification The notification to send.
     * @return The result of the operation.
     * @throws NotificationException if a recoverable error occurs during sending.
     */
    NotificationResult send(Notification notification);
}
