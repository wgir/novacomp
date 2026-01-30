package com.novacomp.notifications.api;

/**
 * Marker interface for all notification types.
 * All notification models (Email, SMS, Push) must implement this.
 */
public interface Notification {
    /**
     * Validates the fields of the notification.
     *
     * @return The result of the validation.
     */
    ValidationResult validate();
}
