package br.com.zup.proposta.proposal;

import br.com.zup.proposta.common.Address;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class NewProposalResponse {

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

    @JsonProperty
    final Address address;

    @JsonProperty
    final ProposalStatus status;

    @JsonCreator
    public NewProposalResponse(@NotBlank String document, @NotBlank String email, @NotBlank String name, @NotNull @Positive BigDecimal salary, Address address, ProposalStatus status) {
        this.document = document;
        this.email = email;
        this.name = name;
        this.salary = salary;
        this.address = address;
        this.status = status;
    }

    public static NewProposalResponse of(Proposal proposal){
        return  new NewProposalResponse(proposal.getDocument(),
                proposal.getEmail(),
                proposal.getName(),
                proposal.getSalary(),
                proposal.getAddress(),
                proposal.getStatus());
    }
}
