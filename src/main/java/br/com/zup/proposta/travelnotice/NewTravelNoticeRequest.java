package br.com.zup.proposta.travelnotice;

import br.com.zup.proposta.common.AddressRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

class NewTravelNoticeRequest {

    @JsonProperty
    @Embedded
    @Valid
    @NotNull
    final AddressRequest destination;

    @JsonProperty
    @FutureOrPresent
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    final LocalDate endOfTravel;

    @JsonProperty
    final String clientIp;

    @JsonProperty
    final String userAgent;

    @JsonCreator
    NewTravelNoticeRequest(AddressRequest destination, LocalDate endOfTravel, String clientIp, String userAgent) {
        this.destination = destination;
        this.endOfTravel = endOfTravel;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }

    TravelNotice toTravelNotice() {
        return new TravelNotice(destination.toAdress(), endOfTravel, clientIp, userAgent);
    }

    @Override
    public String toString() {
        return "NewTravelNoticeRequest{" +
                "destination=" + destination +
                ", endOfTravel=" + endOfTravel +
                ", clientIp='" + clientIp + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }

    public AddressRequest getDestination() {
        return destination;
    }

    public LocalDate getEndOfTravel() {
        return endOfTravel;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
