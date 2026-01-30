package com.novacomp.notifications.channel.push;

import com.novacomp.notifications.api.Notification;
import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a Push notification.
 * Immutable using Lombok @Builder and @Getter.
 */
@Getter
@Builder
public class PushNotification implements Notification {
    @NonNull
    private final String token;
    @NonNull
    private final String title;
    @NonNull
    private final String body;
    @Builder.Default
    private final Map<String, String> data = Collections.emptyMap();
}
