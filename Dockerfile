FROM maven:3.8-jdk-11 AS build
COPY src /app/src
COPY pom.xml /app/pom.xml
VOLUME ~/.m2
RUN mvn -f /app/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]

#FROM openjdk:11-jre-slim
#COPY target/*.jar /app/app.jar
#ENTRYPOINT ["java","-jar","app.jar"]