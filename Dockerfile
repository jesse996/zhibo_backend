#FROM timbru31/java-node:latest
FROM openjdk:11-jre-slim
COPY ./target/zhibo-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java","-jar","app.jar"]