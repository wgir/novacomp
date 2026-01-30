package com.novacomp.notifications.channel.email;

/**
 * Interface for Email Service Providers (ESP).
 * Implementations wrapper specific logic for SendGrid, Mailgun, etc.
 */
public interface EmailProvider {
    /**
     * Sends an email using the provider.
     * 
     * @param notification The email details.
     * @return true if sent successfully, false otherwise.
     * @throws Exception if a communication error occurs.
     */
    boolean sendEmail(EmailNotification notification) throws Exception;

    /**
     * @return The name of the provider (e.g., "SendGrid").
     */
    String getProviderName();
}
