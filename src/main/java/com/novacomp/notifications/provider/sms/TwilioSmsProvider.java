package com.novacomp.notifications.provider.sms;

import com.novacomp.notifications.channel.sms.SmsNotification;
import com.novacomp.notifications.channel.sms.SmsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Stub implementation for Twilio.
 * DOES NOT perform actual HTTP requests.
 */
@Slf4j
@RequiredArgsConstructor
public class TwilioSmsProvider implements SmsProvider {

    private final String accountSid;
    private final String authToken;
    private final String fromPhoneNumber;

    @Override
    public boolean sendSms(SmsNotification notification) {
        // Validation simulation
        if (accountSid == null || authToken == null) {
            throw new IllegalStateException("Twilio credentials are missing");
        }

        // Logic simulation
        log.info("[Twilio] Connecting with SID: {}", mask(accountSid));
        log.info("[Twilio] Sending from: {} to: {}", fromPhoneNumber, notification.getPhoneNumber());
        log.info("[Twilio] Message: {}", notification.getMessage());

        return true;
    }

    @Override
    public String getProviderName() {
        return "Twilio";
    }

    private String mask(String input) {
        if (input == null || input.length() < 4)
            return "****";
        return input.substring(0, 4) + "****";
    }
}
