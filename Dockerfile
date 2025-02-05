# Use an Alpine base image with OpenJDK 17
FROM openjdk:17-alpine

# Create a directory for the app
RUN mkdir /app

# Set the working directory
WORKDIR /app

# Copy the application's jar file into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN apk add --no-cache \
    freetype \
    fontconfig \
    ttf-dejavu

# Copy the keystore into the container
#COPY src/main/resources/keystore.p12 /app/resources/keystore.p12

# Set environment variable to point to the credentials path
#ENV FIREBASE_CREDENTIALS_PATH=/app/firebase-adminsdk.json

# Set environment variables for the application
ENV SPRING_JACKSON_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
ENV SPRING_JACKSON_TIME_ZONE="Asia/Bangkok"
# Expose port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]