FROM openjdk:17-jdk-slim
WORKDIR /app
COPY java_app/SimpleBanking/target/simple-banking-1.0-SNAPSHOT.jar simple-banking.jar
EXPOSE 8080
CMD ["java", "-jar", "simple-banking.jar"]
