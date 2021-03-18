package br.com.zup.proposta.proposal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    Address(String street, int number, String cep) {
        this.street = street;
        this.number = number;
        this.cep = cep;
    }

    Address() {
    }
}
