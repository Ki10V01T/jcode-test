FROM maven:3.9.9-jdk-17 AS builder
COPY ./ ./
RUN mvn clean package -DskipTests
FROM openjdk:17.0.2-slim-buster
COPY --from=builder /target/jcode-test-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 10100
ENTRYPOINT ["java","-jar","/app.jar"]