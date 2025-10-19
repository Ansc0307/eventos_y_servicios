FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/ms-notifications-*.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]