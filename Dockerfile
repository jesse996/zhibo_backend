#FROM openjdk:11-jre-slim
#COPY ./target/zhibo-0.0.1-SNAPSHOT.jar /app/app.jar
#WORKDIR /app
#ENTRYPOINT ["java","-jar","app.jar"]

FROM timbru31/java-node:latest
COPY ./target/zhibo-0.0.1-SNAPSHOT.jar /app/app.jar
COPY ./zhibo_spider /app/zhibo_spider
RUN cd /app/zhibo_spider&&yarn install
WORKDIR /app
ENTRYPOINT ["java","-jar","app.jar"]