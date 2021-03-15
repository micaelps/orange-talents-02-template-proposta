package br.com.zup.proposta.newproposal;

import br.com.zup.proposta.validators.CPForCNPJ;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Optional;


class NewProposalRequest {

    @NotBlank
    @CPForCNPJ
    @JsonProperty
    final String document;

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

    @JsonCreator
    NewProposalRequest(String document, String email, String name, BigDecimal salary) {
        this.document = document;
        this.email = email;
        this.name = name;
        this.salary = salary;
    }

    Proposal toProposal(){
        return new Proposal(onlyDigits(this.document),
                            this.email,
                            this.name,
                            this.salary);
    }


    private String onlyDigits(String cpfOrCnpj){
        return cpfOrCnpj.replaceAll( "\\D*", "" );
    }

    boolean existsProposal(AllProposals allProposals, String document) {
        Optional<Proposal> possibleProposal =  allProposals.findByDocument(document);
        return possibleProposal.isPresent();
    }
}
