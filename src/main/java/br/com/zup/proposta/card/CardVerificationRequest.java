package br.com.zup.proposta.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardVerificationRequest {
//        {
//            "documento": "393.896.040-01",
//                "nome": "Bob",
//                "idProposta": "husaidkjhadagasd"
//        }


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
