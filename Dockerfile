# Use the official OpenJDK 17 image as base
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "notification-service-0.0.1-SNAPSHOT.jar"]