package br.com.zup.proposta.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardVerificationRequest {

    @JsonProperty("documento")
    private String document;


    @JsonProperty("nome")
    private String nome;

    @JsonProperty("idProposta")
    private String proposalId;

    @JsonCreator
    public CardVerificationRequest(String document, String nome, String proposalId) {
        this.document = document;
        this.nome = nome;
        this.proposalId = proposalId;
    }
}
