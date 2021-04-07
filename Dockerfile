#FROM maven:3.8-jdk-11 AS build
#COPY . /app
#RUN mvn -f /app/pom.xml clean package

#FROM openjdk:11-jre-slim
#ARG DEPENDENCY=/app/target
#COPY --from=build ${DEPENDENCY}/*.jar /app/app.jar
#ENTRYPOINT ["java","-jar","app.jar"]

FROM openjdk:11-jre-slim
COPY target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]