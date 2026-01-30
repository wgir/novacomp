package com.novacomp.notifications.channel.email;

import com.novacomp.notifications.api.Notification;
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
}
