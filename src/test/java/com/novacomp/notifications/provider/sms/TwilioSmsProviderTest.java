package com.novacomp.notifications.provider.sms;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.channel.sms.SmsNotification;
import com.novacomp.notifications.channel.sms.SmsSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TwilioSmsProviderTest {

    @Test
    void twilioProvider_WithNullAccountSid_ShouldThrowException() {
        TwilioSmsProvider twilioProvider = new TwilioSmsProvider(null, "auth_token", "+1234567890");
        SmsSender smsSender = new SmsSender(twilioProvider);

        SmsNotification notification = SmsNotification.builder()
                .phoneNumber("+9876543210")
                .message("Test message")
                .build();

        NotificationException exception = assertThrows(
                NotificationException.class,
                () -> smsSender.send(notification));

        assertEquals("Twilio credentials are missing", exception.getCause().getMessage());
    }

    @Test
    void twilioProvider_WithNullAuthToken_ShouldThrowException() {
        TwilioSmsProvider twilioProvider = new TwilioSmsProvider("account_sid", null, "+1234567890");
        SmsSender smsSender = new SmsSender(twilioProvider);

        SmsNotification notification = SmsNotification.builder()
                .phoneNumber("+9876543210")
                .message("Test message")
                .build();

        NotificationException exception = assertThrows(
                NotificationException.class,
                () -> smsSender.send(notification));

        assertEquals("Twilio credentials are missing", exception.getCause().getMessage());
    }
}
