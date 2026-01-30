package com.novacomp.notifications.channel.sms;

import com.novacomp.notifications.api.Notification;
import com.novacomp.notifications.api.NotificationChannel;
import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of NotificationChannel for SMS.
 * Delegates the actual sending to a configured SmsProvider.
 */
@Slf4j
@RequiredArgsConstructor
public class SmsSender implements NotificationChannel {

    private final SmsProvider provider;

    @Override
    public NotificationResult send(Notification notification) {
        if (!(notification instanceof SmsNotification smsNotification)) {
            return NotificationResult.failure("SMS", provider.getProviderName(),
                    "Invalid notification type. Expected SmsNotification.");
        }

        try {
            log.info("Sending SMS to {} via {}", smsNotification.getPhoneNumber(), provider.getProviderName());
            boolean sent = provider.sendSms(smsNotification);

            if (sent) {
                return NotificationResult.success("SMS", provider.getProviderName(), "generated-sms-id");
            } else {
                return NotificationResult.failure("SMS", provider.getProviderName(), "Provider returned failure.");
            }
        } catch (Exception e) {
            log.error("Failed to send SMS", e);
            throw new NotificationException("Failed to send SMS via " + provider.getProviderName(), e);
        }
    }
}
