package com.novacomp.notifications.channel.push;

/**
 * Interface for Push Notification Providers.
 * Implementations wrapper specific logic for FCM, APNS, etc.
 */
public interface PushProvider {
    /**
     * Sends a push notification using the provider.
     * 
     * @param notification The push details.
     * @return true if sent successfully, false otherwise.
     * @throws Exception if a communication error occurs.
     */
    boolean sendPush(PushNotification notification) throws Exception;

    /**
     * @return The name of the provider (e.g., "Firebase").
     */
    String getProviderName();
}
