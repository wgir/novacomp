package com.novacomp.notifications.channel.sms;

import com.novacomp.notifications.api.NotificationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
}
