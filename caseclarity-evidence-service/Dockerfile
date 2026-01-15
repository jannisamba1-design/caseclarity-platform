# ----------- Stage 1: Build -----------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# ----------- Stage 2: Runtime -----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy only the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose internal port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
