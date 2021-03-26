package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;

class NewBiometryRequest {

    @NotBlank
    @JsonProperty
    final String biometry;

    @JsonCreator
    NewBiometryRequest(String biometry) {
        this.biometry = biometry;
    }

    Biometry toModel(Card card) {
        return new Biometry(card, biometry);
    }

    String getBiometry() {
        return biometry;
    }
}
