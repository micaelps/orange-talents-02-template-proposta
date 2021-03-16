package br.com.zup.proposta.newproposal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class Address {

    @JsonProperty
    private String street;

    @JsonProperty
    private int number;

    @JsonProperty
    private String cep;

    @JsonCreator
    public Address(String street, int number, String cep) {
        this.street = street;
        this.number = number;
        this.cep = cep;
    }

    public Address() {
    }
}
