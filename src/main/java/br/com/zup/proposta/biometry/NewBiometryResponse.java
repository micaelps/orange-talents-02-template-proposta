package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.card.CardResponse;
import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class NewBiometryResponse {

    @JsonProperty
    final CardResponse card;
    @JsonProperty
    final LocalDateTime createdAt;
    @JsonProperty
    final String base64;

    @JsonCreator
    NewBiometryResponse(CardResponse card, LocalDateTime createdAt, String base64) {
        this.card = card;
        this.createdAt = createdAt;
        this.base64 = base64;
    }

    static NewBiometryResponse of(Biometry biometry){
        return new NewBiometryResponse(CardResponse.of(biometry.getCard()), biometry.getCreatedAt(), biometry.getBase64());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewBiometryResponse that = (NewBiometryResponse) o;
        return card.equals(that.card) && base64.equals(that.base64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, base64);
    }
}
