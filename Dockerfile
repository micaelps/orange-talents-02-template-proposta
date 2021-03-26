FROM openjdk:11
MAINTAINER Micael Pinheiro da Silva
ENV URL_MYSQL="jdbc:mysql://mysql:3306/proposta?createDatabaseIfNotExist=true"
ENV FINANCIAL_VERIFICATION_API=http://analise:9999
ENV CARD_VERIFICATION_API=http://contas:8888
ENV AUTH_URL=http://keycloak:18080/auth/realms/proposta/protocol/openid-connect/token
ENV KEYCLOAK_JWKS_URI=http://keycloak:18080/auth/realms/proposta/protocol/openid-connect/certs
ENV KEYCLOAK_ISSUER_URI=http://keycloak:18080/auth/realms/proposta
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java","-jar","/app.jar" ]