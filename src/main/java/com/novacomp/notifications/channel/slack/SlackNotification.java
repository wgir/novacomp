package com.novacomp.notifications.channel.slack;

import com.novacomp.notifications.api.Notification;
import com.novacomp.notifications.api.ValidationResult;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a Slack notification.
 */
@Getter
@Builder
public class SlackNotification implements Notification {
    @NonNull
    private final String channel;
    @NonNull
    private final String text;
    private final String username;
    private final String iconEmoji;

    @Override
    public ValidationResult validate() {
        if (channel == null || channel.isBlank()) {
            return ValidationResult.failure("Slack channel cannot be empty");
        }
        if (text == null || text.isBlank()) {
            return ValidationResult.failure("Slack message text cannot be empty");
        }
        return ValidationResult.success();
    }
}
