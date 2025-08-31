# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Runtime stage ----
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/metro-planner-1.0.jar app.jar
EXPOSE 12001
ENTRYPOINT ["java", "-jar", "app.jar"]
