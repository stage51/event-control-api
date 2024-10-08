# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/error-0.0.1-SNAPSHOT.jar errors.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "errors.jar"]
