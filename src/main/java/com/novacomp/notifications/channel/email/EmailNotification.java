package com.novacomp.notifications.channel.email;

import com.novacomp.notifications.api.Notification;
import com.novacomp.notifications.api.ValidationResult;
import java.io.File;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents an Email notification.
 * Immutable using Lombok @Builder and @Getter.
 */
@Getter
@Builder
public class EmailNotification implements Notification {
    @NonNull
    private final String to;
    @NonNull
    private final String subject;
    @NonNull
    private final String body;
    private final String from;
    @Builder.Default
    private final List<File> attachments = Collections.emptyList();
    @Builder.Default
    private final List<String> cc = Collections.emptyList();
    @Builder.Default
    private final List<String> bcc = Collections.emptyList();

    @Override
    public ValidationResult validate() {
        if (to == null || to.isBlank() || !to.contains("@")) {
            return ValidationResult.failure("Invalid recipient email: " + to);
        }
        if (subject == null || subject.isBlank()) {
            return ValidationResult.failure("Email subject cannot be empty");
        }
        if (body == null || body.isBlank()) {
            return ValidationResult.failure("Email body cannot be empty");
        }
        return ValidationResult.success();
    }
}
