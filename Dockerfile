# Use the official OpenJDK 19 image as the base image
FROM openjdk:19-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY build/libs/ios-notification-pushy-0.0.1-SNAPSHOT.jar .

# Specify the command to run your application
CMD ["java", "-jar", "ios-notification-pushy-0.0.1-SNAPSHOT.jar"]
