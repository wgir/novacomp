package com.novacomp.notifications.channel.push;

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
class PushSenderTest {

    @Mock
    private PushProvider provider;

    @InjectMocks
    private PushSender sender;

    @Test
    void send_ShouldReturnSuccess() throws Exception {
        when(provider.getProviderName()).thenReturn("FCM");
        when(provider.sendPush(any(PushNotification.class))).thenReturn(true);

        PushNotification notification = PushNotification.builder()
                .token("device_token")
                .title("Title")
                .body("Body")
                .build();

        NotificationResult result = sender.send(notification);

        assertTrue(result.success());
    }
}
