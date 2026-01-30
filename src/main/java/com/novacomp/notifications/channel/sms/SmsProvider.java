package com.novacomp.notifications.channel.sms;

/**
 * Interface for SMS Service Providers.
 * Implementations wrapper specific logic for Twilio, Vonage, etc.
 */
public interface SmsProvider {
    /**
     * Sends an SMS using the provider.
     * 
     * @param notification The SMS details.
     * @return true if sent successfully, false otherwise.
     * @throws Exception if a communication error occurs.
     */
    boolean sendSms(SmsNotification notification) throws Exception;

    /**
     * @return The name of the provider (e.g., "Twilio").
     */
    String getProviderName();
}
