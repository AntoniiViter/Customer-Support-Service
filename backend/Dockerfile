# Use an official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and build files to the container
COPY ./gradlew /app/gradlew
COPY ./gradle /app/gradle
COPY ./build.gradle /app/build.gradle
COPY ./settings.gradle /app/settings.gradle

# Copy the source code to the container
COPY ./src /app/src

# Grant permission for the gradlew script to be executable
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build -x test

# Copy the built jar file to the container
COPY ./build/libs/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]