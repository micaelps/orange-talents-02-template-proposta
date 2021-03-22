package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;

public class NewBiometryRequest {

    @NotBlank
    @JsonProperty
    private final String biometry;

    @JsonCreator
    public NewBiometryRequest(String biometry) {
        this.biometry = biometry;
    }

    public Biometry toModel(Card card) {
        return new Biometry(card, biometry);
    }

    public String getBiometry() {
        return biometry;
    }
}
