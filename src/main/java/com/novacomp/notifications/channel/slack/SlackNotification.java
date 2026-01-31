package com.novacomp.notifications.channel.slack;

import com.novacomp.notifications.api.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a Slack notification.
 * Immutable using Lombok @Builder and @Getter.
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
}
