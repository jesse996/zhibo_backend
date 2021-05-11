FROM maven:3.8-jdk-11 AS build
COPY src /app/src
WORKDIR /app
COPY pom.xml /app/pom.xml
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

#FROM openjdk:11-jre-slim
#COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
