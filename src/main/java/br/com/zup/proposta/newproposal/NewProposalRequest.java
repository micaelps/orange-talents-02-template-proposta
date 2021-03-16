package br.com.zup.proposta.newproposal;

import br.com.zup.proposta.validators.CPForCNPJ;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;


class NewProposalRequest {

    @NotBlank
    @CPForCNPJ
    @JsonProperty
    final String document ;

    @NotBlank
    @JsonProperty
    final String email;

    @NotBlank
    @JsonProperty
    final String name;

    @NotNull
    @Positive
    @JsonProperty
    final BigDecimal salary;

    @JsonProperty
    @Valid
    final AddressRequest address;


    @JsonCreator
    NewProposalRequest(String document, String email, String name, BigDecimal salary, AddressRequest address) {
        this.document = document;
        this.email = email;
        this.name = name;
        this.salary = salary;
        this.address = address;
    }


    Proposal toProposal(){
        return new Proposal(this.document,
                            this.email,
                            this.name,
                            this.salary,
                            this.address.toAdress());
    }

    @Deprecated
    public AddressRequest getAddress() {
        return address;
    }
}
