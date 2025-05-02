FROM gradle:8.13-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY commons commons/
COPY booking-service booking-service/
RUN gradle :booking-service:build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/booking-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
