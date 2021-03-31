package br.com.zup.proposta.blockedcard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewBlockedCardRequest {

    @JsonProperty
    final String responsibleSystem;

    @JsonCreator
    NewBlockedCardRequest(String responsibleSystem) {
        this.responsibleSystem = responsibleSystem;
    }
}
