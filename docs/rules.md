# LLM Rules – Framework-Agnostic Notifications Library (Java)

This document defines **mandatory rules** that an LLM (or any developer) must follow when generating code for this notifications library.

The goal is to produce a **framework-agnostic, extensible, and clean Java library**, focused on architecture and design rather than integrations.

---

## 1. Core Principles (Non-Negotiable)

### 1.1 Framework Agnosticism
The generated code **MUST**:

- NOT depend on any framework (Spring, Quarkus, Micronaut, etc.)
- NOT use annotations such as `@Component`, `@Service`, `@Autowired`
- NOT use external configuration files (`application.yml`, `.properties`)
- Be fully configurable using **plain Java code**
- Be usable with simple `new` constructors

If a class cannot be instantiated using `new`, the design is incorrect.

---

### 1.2 Dependency Inversion (DIP)
- High-level modules **MUST NOT** depend on concrete implementations
- All external integrations **MUST** be defined via interfaces
- Providers (SendGrid, Twilio, Firebase, etc.) **MUST** implement interfaces

Concrete implementations may be replaced without changing client code.

---

### 1.3 Open / Closed Principle
- Adding a new notification channel **MUST NOT** require modifying existing code
- Adding a new provider **MUST NOT** require modifying existing code
- Extension must be achieved via new implementations, not conditionals

Any usage of `if/else` or `switch` for provider selection is considered a violation.

com.novacomp.notifications
notifications-lib/
├── pom.xml
├── README.md
├── Dockerfile               # optional
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── novacomp/
│   │               └── notifications/
│   │
│   │                   ├── api/
│   │                   │   ├── Notification.java
│   │                   │   ├── NotificationChannel.java
│   │                   │   ├── NotificationSender.java
│   │                   │   ├── NotificationResult.java
│   │                   │   └── NotificationException.java
│   │
│   │                   ├── channel/
│   │                   │   ├── email/
│   │                   │   │   ├── EmailNotification.java
│   │                   │   │   ├── EmailSender.java
│   │                   │   │   └── EmailProvider.java
│   │                   │   │
│   │                   │   ├── sms/
│   │                   │   │   ├── SmsNotification.java
│   │                   │   │   ├── SmsSender.java
│   │                   │   │   └── SmsProvider.java
│   │                   │   │
│   │                   │   └── push/
│   │                   │       ├── PushNotification.java
│   │                   │       ├── PushSender.java
│   │                   │       └── PushProvider.java
│   │
│   │                   ├── provider/
│   │                   │   ├── email/
│   │                   │   │   └── SendGridEmailProvider.java
│   │                   │   │
│   │                   │   ├── sms/
│   │                   │   │   └── TwilioSmsProvider.java
│   │                   │   │
│   │                   │   └── push/
│   │                   │       └── FirebasePushProvider.java
│   │
│   │                   ├── factory/
│   │                   │   └── NotificationSenderFactory.java
│   │
│   │                   ├── validation/
│   │                   │   ├── EmailValidator.java
│   │                   │   ├── SmsValidator.java
│   │                   │   └── PushValidator.java
│   │
│   │                   ├── async/              # optional
│   │                   │   └── AsyncNotificationSender.java
│   │
│   │                   └── examples/
│   │                       └── NotificationExample.java
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── notifications/
│                       └── MainTest.java
```

## 3. Code Quality Rules

### 3.1 Immutability
- All data models **MUST** be immutable
- Use Java records or private fields with getters
- No setters allowed

---

### 3.2 Error Handling
- Use **checked exceptions** for recoverable errors
- Use **runtime exceptions** for programming errors
- Provide clear error messages
- Do NOT swallow exceptions

---

### 3.3 Logging
- Use **SLF4J** for logging
- Log at appropriate levels (INFO, WARN, ERROR)
- Do NOT log sensitive data (API keys, passwords)

---

### 3.4 Testing
- Every channel **MUST** have unit tests
- Use **Mockito** for mocking
- Test both success and failure scenarios
- Test configuration loading

---

## 4. Configuration Rules

### 4.1 Configuration via Code
Configuration **MUST** be done through:

```java
// Valid
NotificationService service = new NotificationService(
    new EmailConfig("smtp.example.com", 587, "user", "pass"),
    new SmsConfig("api.twilio.com", "token")
);

// Invalid
@Configuration
class AppConfig {
    @Bean
    public NotificationService service() {
        return new NotificationService(...);
    }
}
```

---

### 4.2 Provider Configuration
Each provider **MUST** have its own configuration class:

```java
class EmailConfig {
    String host;
    int port;
    String username;
    String password;
}

class SmsConfig {
    String endpoint;
    String apiKey;
}
```

---

## 5. Channel Rules

### 5.1 Required Channels
- Email
- SMS
- Push Notification

### 5.2 Optional Channels
- Slack
- WhatsApp
- Telegram

### 5.3 Channel Interface
All channels **MUST** implement:

```java
interface NotificationChannel {
    Result send(NotificationRequest request);
}
```

Where `Result` is:

```java
class Result {
    boolean success;
    String message;
    String channel;
    String provider;
}