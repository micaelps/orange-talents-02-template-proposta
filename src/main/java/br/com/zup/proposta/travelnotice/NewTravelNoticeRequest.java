package br.com.zup.proposta.travelnotice;

import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.common.AddressRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

class NewTravelNoticeRequest {

    @JsonProperty
    @Embedded
    @Valid
    final AddressRequest destination;

    @JsonProperty
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    final LocalDate endOfTravel;


    @JsonCreator
    NewTravelNoticeRequest(AddressRequest destination, LocalDate endOfTravel) {
        this.destination = destination;
        this.endOfTravel = endOfTravel;
    }

    TravelNotice toTravelNotice(String clientIp, String userAgent, Card card) {
        return new TravelNotice(destination.toAdress(), endOfTravel, clientIp, userAgent, card);
    }

    @Override
    public String toString() {
        return "NewTravelNoticeRequest{" +
                "destination=" + destination +
                ", endOfTravel=" + endOfTravel +
                '}';
    }

    public AddressRequest getDestination() {
        return destination;
    }

    public LocalDate getEndOfTravel() {
        return endOfTravel;
    }
}
