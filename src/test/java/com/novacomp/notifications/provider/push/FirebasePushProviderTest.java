package com.novacomp.notifications.provider.push;

import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.channel.push.PushNotification;
import com.novacomp.notifications.channel.push.PushSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FirebasePushProviderTest {

    @Test
    void firebaseProvider_WithNullProjectId_ShouldThrowException() {
        FirebasePushProvider firebaseProvider = new FirebasePushProvider(null, "path/to/key.json");
        PushSender pushSender = new PushSender(firebaseProvider);

        PushNotification notification = PushNotification.builder()
                .token("device_token")
                .title("Title")
                .body("Body")
                .build();

        NotificationException exception = assertThrows(
                NotificationException.class,
                () -> pushSender.send(notification));

        assertEquals("Firebase credentials are missing", exception.getCause().getMessage());
    }

    @Test
    void firebaseProvider_WithNullServiceAccountKeyPath_ShouldThrowException() {
        FirebasePushProvider firebaseProvider = new FirebasePushProvider("test-project-id", null);
        PushSender pushSender = new PushSender(firebaseProvider);

        PushNotification notification = PushNotification.builder()
                .token("device_token")
                .title("Title")
                .body("Body")
                .build();

        NotificationException exception = assertThrows(
                NotificationException.class,
                () -> pushSender.send(notification));

        assertEquals("Firebase credentials are missing", exception.getCause().getMessage());
    }
}
