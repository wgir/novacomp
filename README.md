# Notifications Library

A framework-agnostic, extensible Java library for sending notifications via multiple channels (Email, SMS, Push).

## Features

- **Framework Agnostic**: Pure Java, no Spring/Quarkus dependencies.
- **Extensible**: Easily add new channels or providers without modifying core code.
- **SOLID Principles**: Adheres to DIP and OCP.
- **Immutable Models**: All notification data is immutable.

## Quick Start

### Installation

Build the project using Maven:

```bash
mvn clean install
```

### Usage

```java
// 1. Configure a Provider
EmailProvider sendGrid = new SendGridEmailProvider("YOUR_API_KEY");

// 2. Create a Channel
NotificationChannel emailChannel = NotificationSenderFactory.createEmailChannel(sendGrid);

// 3. Create a Notification
EmailNotification notification = EmailNotification.builder()
    .to("user@example.com")
    .subject("Hello")
    .body("World")
    .build();

// 4. Send
NotificationResult result = emailChannel.send(notification);

if (result.success()) {
    System.out.println("Sent!");
}
```

## Supported Channels

- **Email**: SendGrid implementation provided.
- **SMS**: Twilio implementation provided.
- **Push**: Firebase (FCM) implementation provided.

## Architecture

The library is designed around the `NotificationChannel` interface. 
`NotificationSender` delegates work to specific `SmsProvider`, `EmailProvider`, etc.

This allows you to swap providers (e.g., Twilio -> Vonage) by simply implementing a new `SmsProvider`, without changing your business logic.

## Running Examples

You can run the included `NotificationExample` class to see the library in action (simulated).

### Using Maven (Recommended)

The easiest way to run the example is using the Maven Exec plugin. I have configured it in the `pom.xml` so you just need to run:

```bash
mvn exec:java
```

### Using Java Command Line

If you want to run the jar manually, you need to handle the classpath separator correctly depending on your OS (`;` for Windows, `:` for Linux/Mac) and shell.

**Windows (PowerShell):**
You must wrap the classpath in quotes to prevent PowerShell from interpreting the `;` as a command separator:
```powershell
java -cp "target/notifications-lib-1.0.0-SNAPSHOT.jar;target/classes" com.novacomp.notifications.examples.NotificationExample
```

**Windows (CMD):**
```cmd
java -cp target/notifications-lib-1.0.0-SNAPSHOT.jar;target/classes com.novacomp.notifications.examples.NotificationExample
```

**Linux / Mac:**
```bash
java -cp target/notifications-lib-1.0.0-SNAPSHOT.jar:target/classes com.novacomp.notifications.examples.NotificationExample
```

### Using Docker

A `Dockerfile` is provided to easily run the example in an isolated environment.

   ```bash
   docker build -t notifications-lib-example .
   ```
   
   Now you can see the image in 
   
   ```bash
   docker image ls
   ```

2. Run the container:
   ```bash
   docker run --rm notifications-lib-example
   

## Running Specific Tests

To run unit tests for a specific channel, use the `-Dtest` property with Maven:

**Email Channel:**
```bash
mvn -Dtest=EmailSenderTest test
```

**SMS Channel:**
```bash
mvn -Dtest=SmsSenderTest test
```

**Push Channel:**
```bash
mvn -Dtest=PushSenderTest test
```

## AI Usage
This project was implemented with the assistance of an AI agent (Gemini based) to generate the boilerplate code, structure the project according to SOLID principles, and ensure adherence to the requirements. The AI suggested the factory pattern and the specific breakdown of provider interfaces.
