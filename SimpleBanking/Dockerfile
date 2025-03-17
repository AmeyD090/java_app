# Stage 1: Build stage
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY target/simple-banking-1.0-SNAPSHOT.jar simple-banking.jar

# Stage 2: Runtime stage
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=build /app/simple-banking.jar simple-banking.jar
EXPOSE 8080
CMD ["java", "-jar", "simple-banking.jar"]
