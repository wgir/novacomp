# Notifications Library

A framework-agnostic, extensible Java library for sending notifications via multiple channels (Email, SMS, Push).

## Features

- **Framework Agnostic**: Pure Java, no Spring/Quarkus dependencies.
- **Async Support**: Native `CompletableFuture` support for non-blocking operations.
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

// 4. Send (Sync)
NotificationResult result = emailChannel.send(notification);

// 5. Send (Async)
emailChannel.sendAsync(notification)
    .thenAccept(res -> System.out.println("Async result: " + res.success()));
```

## Supported Channels

- **Email**: SendGrid implementation provided.
- **SMS**: Twilio implementation provided.
- **Push**: Firebase (FCM) implementation provided.

## Architecture

The library is designed around the `NotificationChannel` interface. 
`NotificationSender` delegates work to specific `SmsProvider`, `EmailProvider`, etc.

This allows you to swap providers (e.g., Twilio -> Vonage) by simply implementing a new `SmsProvider`, without changing your business logic.

## Asynchronous Notifications

The library provides native support for asynchronous notification sending via `CompletableFuture`.

### Default Async
Uses the `ForkJoinPool.commonPool()` per default `CompletableFuture` behavior.
```java
channel.sendAsync(notification)
    .thenAccept(result -> {
        if (result.success()) {
            log.info("Async send success");
        }
    });
```

### Custom Executor (Recommended for Production)
For better control over resources, pass a custom `Executor`.
```java
ExecutorService executor = Executors.newFixedThreadPool(10);
channel.sendAsync(notification, executor)
    .handle((result, ex) -> {
        if (ex != null) log.error("Error", ex);
        return result;
    });
```

> [!IMPORTANT]
> **Technical Considerations: Thread Management**
> 
> While it may be tempting to create executors with a large number of threads (e.g., `newFixedThreadPool(1000)`), this can lead to several issues:
> 
> 1. **Thread Exhaustion**: Every thread consumes memory (stack size). Creating thousands of threads can lead to `OutOfMemoryError: unable to create new native thread`.
> 2. **Context Switching Overhead**: The CPU spends more time switching between threads than actually executing code, severely degrading performance.
> 3. **Socket/Connection Limits**: Providers (like Twilio or SendGrid) have rate limits. A high thread count might overwhelm the provider's API or exhaust local socket connections.
> 4. **Resource Leakage**: Always ensure you shut down custom executors (`executor.shutdown()`) when they are no longer needed to prevent memory leaks.

### Sizing your Thread Pool

Since notification sending is an **I/O-bound** task (most of the time is spent waiting for the network), you can generally afford a higher thread count than for CPU-bound tasks.

A common formula to calculate the optimal thread pool size (from *Java Concurrency in Practice*) is:

**`N_threads = N_cores * U_cpu * (1 + W/C)`**

Where:
- **`N_cores`**: Number of available CPUs (`Runtime.getRuntime().availableProcessors()`).
- **`U_cpu`**: Target CPU utilization (0 <= U_cpu <= 1).
- **`W/C`**: Ratio of **Wait time** to **Compute time**.

**Practical Example:**
If you have 4 cores, want 50% CPU utilization, and your notification takes 100ms (95ms waiting for API, 5ms processing):
`N_threads = 4 * 0.5 * (1 + 95/5) = 2 * 20 = 40 threads`.

**Recommendation:**
- For simple use cases, a `FixedThreadPool` with **10-20 threads** is often a safe start.
- For high-performance needs, perform load testing to find the "sweet spot" before hitting thread exhaustion.

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
