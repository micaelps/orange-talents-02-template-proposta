package br.com.zup.proposta.proposal.feignClients;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinancialVerificationRequest {

    @JsonProperty("documento")
    private String document;
    @JsonProperty("nome")
    private String name;
    @JsonProperty("idProposta")
    private String idProposal;


    public FinancialVerificationRequest(String document, String name, String idProposal) {
        this.document = document;
        this.name = name;
        this.idProposal = idProposal;
    }

    public String getDocument() {
        return document;
    }

    public String getName() {
        return name;
    }

    public String getIdProposal() {
        return idProposal;
    }
}
