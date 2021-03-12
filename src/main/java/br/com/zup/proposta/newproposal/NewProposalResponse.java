package br.com.zup.proposta.newproposal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

class NewProposalResponse {

    @NotBlank
    @JsonProperty
    final String document;

    @JsonProperty
    @NotBlank
    final String email;

    @NotBlank
    @JsonProperty
    final String name;

    @NotNull
    @Positive
    @JsonProperty
    final BigDecimal salary;

    @JsonCreator
    public NewProposalResponse(@NotBlank String document, @NotBlank String email, @NotBlank String name, @NotNull @Positive BigDecimal salary) {
        this.document = document;
        this.email = email;
        this.name = name;
        this.salary = salary;
    }

    public static NewProposalResponse of(Proposal proposal){
        return  new NewProposalResponse(proposal.getDocument(),
                proposal.getEmail(),
                proposal.getName(),
                proposal.getSalary());
    }

}
