package br.com.zup.proposta.proposal;

import com.fasterxml.jackson.annotation.JsonProperty;

class FinancialVerificationRequest {

    @JsonProperty("documento")
    final String document;
    @JsonProperty("nome")
    final String name;
    @JsonProperty("idProposta")
    final String idProposal;


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
