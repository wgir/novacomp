package com.novacomp.notifications.channel.sms;

import com.novacomp.notifications.api.Notification;
import com.novacomp.notifications.api.ValidationResult;
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

    @Override
    public ValidationResult validate() {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return ValidationResult.failure("Phone number cannot be empty");
        }
        if (message == null || message.isBlank()) {
            return ValidationResult.failure("SMS message cannot be empty");
        }
        return ValidationResult.success();
    }
}
