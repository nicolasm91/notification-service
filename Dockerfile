FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app
COPY pom.xml /app/pom.xml
COPY src /app/src
RUN mvn clean install
RUN mvn clean package

# Use the official OpenJDK 17 image as base
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build app/target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "notification-service-0.0.1-SNAPSHOT.jar"]