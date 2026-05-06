FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/pennypilot-0.0.1-SNAPSHOT.jar pennypilot-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "pennypilot-v1.0.jar"]