package br.com.zup.proposta.proposal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinancialVerificationResponse {

    @JsonProperty("documento")
    private String document;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("idProposta")
    private String idProposal;

    @JsonProperty("resultadoSolicitacao")
    private String resultStatus;


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
