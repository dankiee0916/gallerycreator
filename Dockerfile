# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom first to cache dependencies
COPY pom.xml .
RUN mvn -B -DskipTests=true dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn -B -DskipTests=true package

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the fat jar from the build stage (wildcard handles your jar name)
COPY --from=build /app/target/*.jar app.jar

# Render will pass PORT env; Spring Boot uses it via server.port=${PORT:8080}
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
