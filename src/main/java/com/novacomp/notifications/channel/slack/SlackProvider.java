package com.novacomp.notifications.channel.slack;

/**
 * Interface for Slack notification providers.
 */
public interface SlackProvider {
    boolean sendSlackMessage(SlackNotification notification);

    String getProviderName();
}
