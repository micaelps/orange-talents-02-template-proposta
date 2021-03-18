FROM openjdk:11
MAINTAINER Micael Pinheiro da Silva
ENV URL_MYSQL="jdbc:mysql://mysql:3306/proposta?createDatabaseIfNotExist=true"
ENV FINANCIAL_VERIFICATION_API=http://analise:9999
ENV CARD_VERIFICATION_API=http://contas:8888
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java","-jar","/app.jar" ]