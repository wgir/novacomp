package com.novacomp.notifications.api;

/**
 * Represents the result of a notification sending attempt.
 * Immutable record.
 */
public record NotificationResult(
        boolean success,
        String message,
        String channelName,
        String providerName,
        String messageId) {
    public static NotificationResult success(String channelName, String providerName, String messageId) {
        return new NotificationResult(true, "Success", channelName, providerName, messageId);
    }

    public static NotificationResult failure(String channelName, String providerName, String error) {
        return new NotificationResult(false, error, channelName, providerName, null);
    }
}
