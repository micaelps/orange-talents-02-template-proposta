package br.com.zup.proposta.proposal;

import com.fasterxml.jackson.annotation.JsonProperty;

class FinancialVerificationResponse {

    @JsonProperty("documento")
    final String document;

    @JsonProperty("nome")
    final String name;

    @JsonProperty("idProposta")
    final String idProposal;

    @JsonProperty("resultadoSolicitacao")
    final String resultStatus;


    public FinancialVerificationResponse(String document, String name, String idProposal, String resultStatus) {
        this.document = document;
        this.name = name;
        this.idProposal = idProposal;
        this.resultStatus = resultStatus;
    }

    @Override
    public String toString() {
        return "FinancialVerificationResponse{" +
                "document='" + document + '\'' +
                ", name='" + name + '\'' +
                ", idProposal='" + idProposal + '\'' +
                ", resultStatus='" + resultStatus + '\'' +
                '}';
    }
}
