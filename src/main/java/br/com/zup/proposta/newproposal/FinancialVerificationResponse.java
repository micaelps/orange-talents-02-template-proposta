package br.com.zup.proposta.newproposal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinancialVerificationResponse {

    @JsonProperty("document")
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

}
