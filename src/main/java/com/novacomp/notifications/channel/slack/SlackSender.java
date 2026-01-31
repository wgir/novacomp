package com.novacomp.notifications.channel.slack;

import com.novacomp.notifications.api.Notification;
import com.novacomp.notifications.api.NotificationChannel;
import com.novacomp.notifications.api.NotificationException;
import com.novacomp.notifications.api.NotificationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of NotificationChannel for Slack notifications.
 * Delegates the actual sending to a configured SlackProvider.
 */
@Slf4j
@RequiredArgsConstructor
public class SlackSender implements NotificationChannel {

    private final SlackProvider provider;

    @Override
    public NotificationResult send(Notification notification) {
        if (!(notification instanceof SlackNotification slackNotification)) {
            return NotificationResult.failure("SLACK", provider.getProviderName(),
                    "Invalid notification type. Expected SlackNotification.");
        }

        var validation = slackNotification.validate();
        if (!validation.isValid()) {
            return NotificationResult.failure("SLACK", provider.getProviderName(),
                    "Validation failed: " + String.join(", ", validation.errors()));
        }

        try {
            log.info("Sending Slack message to {} via {}", slackNotification.getChannel(), provider.getProviderName());
            boolean sent = provider.sendSlackMessage(slackNotification);

            if (sent) {
                return NotificationResult.success("SLACK", provider.getProviderName(), "generated-slack-id");
            } else {
                return NotificationResult.failure("SLACK", provider.getProviderName(), "Provider returned failure.");
            }
        } catch (Exception e) {
            log.error("Failed to send Slack message", e);
            throw new NotificationException("Failed to send Slack message via " + provider.getProviderName(), e);
        }
    }
}
