package com.novacomp.notifications.provider.email;

import com.novacomp.notifications.channel.email.EmailNotification;
import com.novacomp.notifications.channel.email.EmailProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Stub implementation for SendGrid.
 * DOES NOT perform actual HTTP requests.
 */
@Slf4j
@RequiredArgsConstructor
public class SendGridEmailProvider implements EmailProvider {

    private final String apiKey;

    @Override
    public boolean sendEmail(EmailNotification notification) {
        // Validation simulation
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("SendGrid API Key is missing");
        }

        // Logic simulation
        log.info("[SendGrid] Connecting with key: {}", maskKey(apiKey));
        log.info("[SendGrid] Sending email to: {}", notification.getTo());
        log.info("[SendGrid] Subject: {}", notification.getSubject());
        log.info("[SendGrid] Body Length: {}", notification.getBody().length());

        return true;
    }

    @Override
    public String getProviderName() {
        return "SendGrid";
    }

    private String maskKey(String key) {
        if (key == null || key.length() < 4)
            return "****";
        return key.substring(0, 4) + "****";
    }
}
