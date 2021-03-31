package br.com.zup.proposta.portfolioassociation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssociationCardClientRequest {

    @JsonProperty("email")
    final String email;
    @JsonProperty("carteira")
    final String portfolio;

    public AssociationCardClientRequest(String email, String portfolio) {
        this.email = email;
        this.portfolio = portfolio;
    }
}
