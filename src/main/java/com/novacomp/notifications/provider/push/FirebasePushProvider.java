package com.novacomp.notifications.provider.push;

import com.novacomp.notifications.channel.push.PushNotification;
import com.novacomp.notifications.channel.push.PushProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Stub implementation for Firebase Cloud Messaging (FCM).
 * DOES NOT perform actual HTTP requests.
 */
@Slf4j
@RequiredArgsConstructor
public class FirebasePushProvider implements PushProvider {

    private final String projectId;
    private final String serviceAccountKeyPath;

    @Override
    public boolean sendPush(PushNotification notification) {
        // Validation simulation
        if (projectId == null || serviceAccountKeyPath == null) {
            throw new IllegalStateException("Firebase credentials are missing");
        }

        // Logic simulation
        log.info("[FCM] Authenticating with: {}", serviceAccountKeyPath);
        log.info("[FCM] Sending to token: {}", notification.getToken());
        log.info("[FCM] Title: {} | Body: {}", notification.getTitle(), notification.getBody());

        return true;
    }

    @Override
    public String getProviderName() {
        return "Firebase";
    }
}
