package br.com.zup.proposta.config.security;

import com.fasterxml.jackson.annotation.JsonProperty;

class LoginResponse {

    @JsonProperty("token_type")
    final String tokenType;

    @JsonProperty("access_token")
    final String accessToken;

    @JsonProperty("expires_in")
    final Integer expiresIn;

    LoginResponse(String tokenType, String accessToken, Integer expiresIn) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

}