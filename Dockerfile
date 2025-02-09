FROM maven:3.8.7-eclipse-temurin-17 AS builder
LABEL authors="Oleh Mishchenkov"

WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/leonbets-parser-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
