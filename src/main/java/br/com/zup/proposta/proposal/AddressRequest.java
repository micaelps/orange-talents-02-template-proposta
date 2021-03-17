package br.com.zup.proposta.proposal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

class AddressRequest {

    @JsonProperty
    @NotBlank
    private final String street;

    @JsonProperty
    @NotNull
    private final Integer number;

    @JsonProperty
    @NotBlank
    private final String cep;

    @JsonCreator
    public AddressRequest(String street, Integer number, String cep) {
        this.street = street;
        this.number = number;
        this.cep = cep;
    }

    Address toAdress(){
        return new Address(this.street, this.number, this.cep);
    }


    public Integer getNumber() {
        return number;
    }

    public String getCep() {
        return cep;
    }

    public String getStreet() {
        return street;
    }
}
