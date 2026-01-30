package com.novacomp.notifications.provider.slack;

import com.novacomp.notifications.channel.slack.SlackNotification;
import com.novacomp.notifications.channel.slack.SlackProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Stub implementation for Slack Webhook.
 */
@Slf4j
@RequiredArgsConstructor
public class SlackWebhookProvider implements SlackProvider {

    private final String webhookUrl;

    @Override
    public boolean sendSlackMessage(SlackNotification notification) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            throw new IllegalStateException("Slack webhook URL is missing");
        }
        log.info("[Slack] Posting to webhook: {}", webhookUrl);
        return true;
    }

    @Override
    public String getProviderName() {
        return "Slack";
    }
}
