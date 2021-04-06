FROM openjdk:11
MAINTAINER Micael Pinheiro da Silva

ENV URL_MYSQL="jdbc:mysql://mysql:3306/proposta?createDatabaseIfNotExist=true"
ENV FINANCIAL_VERIFICATION_API=http://analise:9999
ENV CARD_VERIFICATION_API=http://contas:8888
ENV AUTH_URL=http://keycloak:8080/auth/realms/nosso-cartao/protocol/openid-connect/token
ENV KEYCLOAK_JWKS_URI=http://keycloak:8080/auth/realms/nosso-cartao/protocol/openid-connect/certs
ENV KEYCLOAK_ISSUER_URI=http://keycloak:8080/auth/realms/nosso-cartao
ENV ENCRYPT.DECRYPT.SECRET=proposta
ENV ENCRYPT.DECRYPT.SALT=f4d4991655ef9f356e8bde66ca3fb9a4
ENV JAEGER_ENDPOINT=http://jaeger:14268/api/traces

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java","-jar","/app.jar" ]