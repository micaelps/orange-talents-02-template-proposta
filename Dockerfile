FROM openjdk:11
MAINTAINER Micael Pinheiro da Silva
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java","-jar","/app.jar" ]