package com.novacomp.notifications.channel.sms;

import com.novacomp.notifications.api.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents an SMS notification.
 * Immutable using Lombok @Builder and @Getter.
 */
@Getter
@Builder
public class SmsNotification implements Notification {
    @NonNull
    private final String phoneNumber;
    @NonNull
    private final String message;
}
