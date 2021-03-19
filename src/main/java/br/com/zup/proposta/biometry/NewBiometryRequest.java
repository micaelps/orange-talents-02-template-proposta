package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.EntityManager;

class NewBiometryRequest {

    @JsonProperty
   private String biometry;

    @JsonCreator
    public NewBiometryRequest(String biometry) {
        this.biometry = biometry;
    }

    public Biometry toModel(Card card) {
        return new Biometry(card, biometry);
    }
}
