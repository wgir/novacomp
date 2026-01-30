package com.novacomp.notifications.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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

    /**
     * Sends a notification asynchronously using the default CompletableFuture
     * executor.
     *
     * @param notification The notification to send.
     * @return A CompletableFuture that will complete with the result of the
     *         operation.
     */
    default CompletableFuture<NotificationResult> sendAsync(Notification notification) {
        return CompletableFuture.supplyAsync(() -> send(notification));
    }

    /**
     * Sends a notification asynchronously using the provided executor.
     *
     * @param notification The notification to send.
     * @param executor     The executor to use for asynchronous execution.
     * @return A CompletableFuture that will complete with the result of the
     *         operation.
     */
    default CompletableFuture<NotificationResult> sendAsync(Notification notification, Executor executor) {
        return CompletableFuture.supplyAsync(() -> send(notification), executor);
    }
}
