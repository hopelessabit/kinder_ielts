# ============================
# Stage 1: Build Application
# ============================
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven dependencies
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ============================
# Stage 2: Runtime Environment
# ============================
FROM openjdk:17-alpine

# Create application directory
WORKDIR /app

# Install necessary fonts and libraries
RUN apk add --no-cache \
    freetype \
    fontconfig \
    ttf-dejavu

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Environment Configuration Files
# These files will be mounted as volumes at runtime
VOLUME ["/app/resources"]

# Environment Variables (to be set dynamically via `key.env`)
ENV SPRING_CONFIG_LOCATION=classpath:/application.properties

# Expose HTTPS Port
EXPOSE 8080

# Default entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
