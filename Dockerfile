FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy Maven wrapper and file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Resolve dependencies
RUN ./mvnw dependency:go-offline

# Copy source
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the example
CMD ["java", "-cp", "target/notifications-lib-1.0.0-SNAPSHOT.jar:target/dependency/*", "com.novacomp.notifications.examples.NotificationExample"]
