package com.novacomp.notifications.channel.sms;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import com.novacomp.notifications.channel.email.EmailNotification;
import com.novacomp.notifications.provider.sms.TwilioSmsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsSenderTest {

        @Mock
        private SmsProvider provider;

        @InjectMocks
        private SmsSender sender;

        @Test
        void send_ShouldReturnSuccess() throws Exception {
                when(provider.getProviderName()).thenReturn("Twilio");
                when(provider.sendSms(any(SmsNotification.class))).thenReturn(true);

                SmsNotification notification = SmsNotification.builder()
                                .phoneNumber("+1234567890")
                                .message("Hello")
                                .build();

                NotificationResult result = sender.send(notification);

                assertTrue(result.success());
        }

        @Test
        void send_WithInvalidNotificationType_ShouldReturnFailure() {
                when(provider.getProviderName()).thenReturn("Twilio");

                EmailNotification emailNotification = EmailNotification.builder()
                                .to("test@example.com")
                                .subject("Test")
                                .body("Test body")
                                .build();

                NotificationResult result = sender.send(emailNotification);

                assertFalse(result.success());
                assertEquals("SMS", result.channelName());
                assertEquals("Twilio", result.providerName());
                assertTrue(result.message().contains("Invalid notification type"));
        }

        @Test
        void send_WithEmptySmsFields_ShouldReturnFailure() {
                when(provider.getProviderName()).thenReturn("Twilio");

                SmsNotification notification = SmsNotification.builder()
                                .phoneNumber("")
                                .message("")
                                .build();

                NotificationResult result = sender.send(notification);

                assertFalse(result.success());
                assertEquals("SMS", result.channelName());
                assertTrue(result.message().contains("Validation failed"));
                assertTrue(result.message().contains("Phone number cannot be empty"));
        }

        @Test
        void send_WhenProviderReturnsFalse_ShouldReturnFailure() throws Exception {
                when(provider.getProviderName()).thenReturn("Twilio");
                when(provider.sendSms(any(SmsNotification.class))).thenReturn(false);

                SmsNotification notification = SmsNotification.builder()
                                .phoneNumber("+1234567890")
                                .message("Hello")
                                .build();

                NotificationResult result = sender.send(notification);

                assertFalse(result.success());
                assertEquals("SMS", result.channelName());
                assertEquals("Twilio", result.providerName());
                assertEquals("Provider returned failure.", result.message());
        }

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
