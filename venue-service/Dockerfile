FROM gradle:8.13-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY commons commons/
COPY venue-service venue-service/
RUN gradle :venue-service:build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/venue-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
