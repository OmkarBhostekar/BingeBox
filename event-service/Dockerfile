FROM gradle:8.13-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY commons commons/
COPY event-service event-service/
RUN gradle :event-service:build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/event-service/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
