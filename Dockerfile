FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/pennypilot-0.0.1-SNAPSHOT.jar pennypilot-v1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pennypilot-v1.0.jar"]