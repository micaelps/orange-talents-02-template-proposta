package br.com.zup.proposta.config.security;

import com.fasterxml.jackson.annotation.JsonProperty;

class LoginRequest {

    @JsonProperty
    final String grantType;

    @JsonProperty
    final String username;

    @JsonProperty
    final String password;

    @JsonProperty
    final String clientId;

    @JsonProperty
    final String scope;


    LoginRequest(String grantType, String username, String password, String clientId, String scope) {
        this.grantType = grantType;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.scope = scope;
    }

}