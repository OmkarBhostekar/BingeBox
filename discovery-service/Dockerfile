FROM gradle:8.13-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY discovery-service discovery-service/
RUN gradle :discovery-service:build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/discovery-service/build/libs/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
