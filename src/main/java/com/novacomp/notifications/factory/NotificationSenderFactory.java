package com.novacomp.notifications.factory;

import com.novacomp.notifications.api.NotificationChannel;
import com.novacomp.notifications.channel.email.EmailSender;
import com.novacomp.notifications.channel.email.EmailProvider;
import com.novacomp.notifications.channel.sms.SmsSender;
import com.novacomp.notifications.channel.sms.SmsProvider;
import com.novacomp.notifications.channel.push.PushSender;
import com.novacomp.notifications.channel.push.PushProvider;
import com.novacomp.notifications.channel.slack.SlackSender;
import com.novacomp.notifications.channel.slack.SlackProvider;

/**
 * Factory class to create Notification Channels.
 * Provides a simple way to instantiate senders with specific providers.
 */
public class NotificationSenderFactory {

    public static NotificationChannel createEmailChannel(EmailProvider provider) {
        return new EmailSender(provider);
    }

    public static NotificationChannel createSmsChannel(SmsProvider provider) {
        return new SmsSender(provider);
    }

    public static NotificationChannel createPushChannel(PushProvider provider) {
        return new PushSender(provider);
    }

    public static NotificationChannel createSlackChannel(SlackProvider provider) {
        return new SlackSender(provider);
    }
}
