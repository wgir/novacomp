package com.novacomp.notifications.examples;

import com.novacomp.notifications.api.NotificationChannel;
import com.novacomp.notifications.api.NotificationResult;
import com.novacomp.notifications.channel.email.EmailNotification;
import com.novacomp.notifications.channel.push.PushNotification;
import com.novacomp.notifications.channel.sms.SmsNotification;
import com.novacomp.notifications.channel.slack.SlackNotification;
import com.novacomp.notifications.factory.NotificationSenderFactory;
import com.novacomp.notifications.provider.email.SendGridEmailProvider;
import com.novacomp.notifications.provider.push.FirebasePushProvider;
import com.novacomp.notifications.provider.sms.TwilioSmsProvider;
import com.novacomp.notifications.provider.slack.SlackWebhookProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Example usage of the Notifications Library.
 * Demonstrates how to configure providers, create channels, and send
 * notifications.
 */
@Slf4j
public class NotificationExample {

    public static void main(String[] args) {
        // 1. Configure Email (SendGrid)
        var emailProvider = new SendGridEmailProvider("SG.1234567890");
        NotificationChannel emailChannel = NotificationSenderFactory.createEmailChannel(emailProvider);

        var email = EmailNotification.builder()
                .to("user@example.com")
                .subject("Welcome!")
                .body("Hello from the Notification Library")
                .build();

        sendAndLog(emailChannel, email);

        // 2. Configure SMS (Twilio)
        var smsProvider = new TwilioSmsProvider("AC12345", "auth_token_xyz", "+15551234567");
        NotificationChannel smsChannel = NotificationSenderFactory.createSmsChannel(smsProvider);

        var sms = SmsNotification.builder()
                .phoneNumber("+19876543210")
                .message("Your verification code is 1234")
                .build();

        sendAndLog(smsChannel, sms);

        // 3. Configure Push (Firebase)
        var pushProvider = new FirebasePushProvider("my-project-id", "/path/to/service-account.json");
        NotificationChannel pushChannel = NotificationSenderFactory.createPushChannel(pushProvider);

        var push = PushNotification.builder()
                .token("device_token_xyz")
                .title("New Alert")
                .body("You have a new message")
                .build();

        sendAndLog(pushChannel, push);

        // 4. Configure Slack (Webhook)
        var slackProvider = new SlackWebhookProvider("https://hooks.slack.com/services/T000/B000/XXXX");
        NotificationChannel slackChannel = NotificationSenderFactory.createSlackChannel(slackProvider);

        var slackNotification = SlackNotification.builder()
                .channel("#notifications")
                .text("A new system alert has been triggered!")
                .username("AlertBot")
                .build();

        sendAndLog(slackChannel, slackNotification);

        // 4. Async Example (Default Executor)
        log.info("--- Starting Async Notification (Default) ---");
        emailChannel.sendAsync(email)
                .thenAccept(result -> log.info("Async (Default) Result: {}", result.success() ? "SUCCESS" : "FAILURE"));

        // 5. Async Example (Custom Executor)
        log.info("--- Starting Async Notification (Custom Executor) ---");
        ExecutorService customExecutor = Executors.newFixedThreadPool(2);
        try {
            smsChannel.sendAsync(sms, customExecutor)
                    .thenAccept(
                            result -> log.info("Async (Custom) Result: {}", result.success() ? "SUCCESS" : "FAILURE"));

            // Wait a bit for async tasks to complete in this example
            customExecutor.shutdown();
            customExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("--- Main Method Finished ---");
    }

    private static void sendAndLog(NotificationChannel channel,
            com.novacomp.notifications.api.Notification notification) {
        try {
            NotificationResult result = channel.send(notification);
            if (result.success()) {
                log.info("✅ Notification sent! ID: {}", result.messageId());
            } else {
                log.error("❌ Failed: {}", result.message());
            }
        } catch (Exception e) {
            log.error("❌ Exception: {}", e.getMessage());
        }
    }
}
