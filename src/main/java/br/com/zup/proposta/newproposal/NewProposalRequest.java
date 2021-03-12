package br.com.zup.proposta.newproposal;

import br.com.zup.proposta.validators.CPForCNPJ;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

class NewProposalRequest {

    @NotBlank
    @CPForCNPJ
    final String document;
    @NotBlank
    final String email;
    @NotBlank
    final String name;
    @NotNull
    @Positive
    final BigDecimal salary;


    NewProposalRequest(String document, String email, String name, BigDecimal salary) {
        this.document = document;
        this.email = email;
        this.name = name;
        this.salary = salary;
    }

    Proposal toProposal(){
        return new Proposal(this.document, this.email, this.name, this.salary);
    }
}
