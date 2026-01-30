package com.novacomp.notifications.api;

import java.util.Collections;
import java.util.List;

/**
 * Represents the result of a notification field validation.
 *
 * @param isValid Whether the validation passed.
 * @param errors  List of error messages if validation failed.
 */
public record ValidationResult(boolean isValid, List<String> errors) {

    /**
     * Creates a successful validation result.
     */
    public static ValidationResult success() {
        return new ValidationResult(true, Collections.emptyList());
    }

    /**
     * Creates a failed validation result with a single error message.
     */
    public static ValidationResult failure(String errorMessage) {
        return new ValidationResult(false, List.of(errorMessage));
    }

    /**
     * Creates a failed validation result with multiple error messages.
     */
    public static ValidationResult failure(List<String> errorMessages) {
        return new ValidationResult(false, errorMessages);
    }
}
