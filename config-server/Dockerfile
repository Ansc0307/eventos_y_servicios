FROM maven:3.9.4-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/config-server.jar
EXPOSE 8888
RUN apk add --no-cache curl
ENTRYPOINT ["java","-jar","/app/config-server.jar"]
