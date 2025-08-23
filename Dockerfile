FROM openjdk:17-alpine
EXPOSE 12001
ADD target/metro-planner-1.0.jar metro-planner-1.0.jar
ENTRYPOINT ["java", "-jar", "/metro-planner-1.0.jar"]