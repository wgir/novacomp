package com.novacomp.notifications.provider.slack;

import com.novacomp.notifications.channel.slack.SlackNotification;
import com.novacomp.notifications.channel.slack.SlackProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Stub implementation for Slack Webhook.
 * DOES NOT perform actual HTTP requests.
 */
@Slf4j
@RequiredArgsConstructor
public class SlackWebhookProvider implements SlackProvider {

    private final String webhookUrl;

    @Override
    public boolean sendSlackMessage(SlackNotification notification) {
        // Validation simulation
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            throw new IllegalStateException("Slack webhook URL is missing");
        }

        // Logic simulation
        log.info("[Slack] Posting to webhook: {}", maskUrl(webhookUrl));
        log.info("[Slack] Channel: {}", notification.getChannel());
        log.info("[Slack] Text: {}", notification.getText());
        if (notification.getUsername() != null) {
            log.info("[Slack] Username: {}", notification.getUsername());
        }
        if (notification.getIconEmoji() != null) {
            log.info("[Slack] Icon: {}", notification.getIconEmoji());
        }

        return true;
    }

    @Override
    public String getProviderName() {
        return "Slack";
    }

    private String maskUrl(String url) {
        if (url == null || url.length() < 20)
            return "https://hooks.slack.com/****";
        return url.substring(0, 30) + "****";
    }
}
