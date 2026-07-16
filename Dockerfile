# Phaase 1: Compilation
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and compile
COPY src ./src
RUN mvn clean package -DskipTests

# Phase 2: Image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR file generated
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Execute application
ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "app.jar"]
