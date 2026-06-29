# ---- Stage 1: Build the application ----
# Use a Maven image that includes JDK 21 to compile the project
FROM maven:3.9-eclipse-temurin-21 AS build

# Set the working directory inside the build container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application, skipping tests (no test suite yet)
RUN mvn clean package -DskipTests

# ---- Stage 2: Run the application ----
# Use a smaller image with just the Java runtime to run the built app
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/myhealth-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (Render provides the actual port via the PORT env variable)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
