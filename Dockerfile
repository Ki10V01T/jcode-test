FROM ubuntu:latest
# FROM maven:3.9.9-eclipse-temurin-17-focal AS builder
# COPY ./ ./
# RUN mvn clean package -DskipTests
# FROM openjdk:17.0.2-slim-buster
# COPY --from=builder /target/jcode-test-0.0.1-SNAPSHOT.jar /app.jar

# ARG DB_USERNAME
# ARG DB_PASSWORD
# ENV DB_USERNAME $DB_USERNAME
# ENV DB_PASSWORD $DB_PASSWORD

EXPOSE 10100
#ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["/bin/bash"]
