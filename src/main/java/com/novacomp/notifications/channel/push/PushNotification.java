package com.novacomp.notifications.channel.push;

import com.novacomp.notifications.api.Notification;
import com.novacomp.notifications.api.ValidationResult;
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

    @Override
    public ValidationResult validate() {
        if (token == null || token.isBlank()) {
            return ValidationResult.failure("Push token cannot be empty");
        }
        if (title == null || title.isBlank()) {
            return ValidationResult.failure("Push title cannot be empty");
        }
        if (body == null || body.isBlank()) {
            return ValidationResult.failure("Push body cannot be empty");
        }
        return ValidationResult.success();
    }
}
