# Use an official Java 17 runtime as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Pre-download dependencies to speed up builds
RUN ./mvnw dependency:go-offline -B

# Copy the entire source code into the container
COPY src ./src

# Build the Spring Boot app (skip tests for faster build)
RUN ./mvnw -DskipTests=true package

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "target/gallerycreator-0.0.1-SNAPSHOT.jar"]
