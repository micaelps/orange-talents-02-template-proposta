package br.com.zup.proposta.travelnotice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TravelNotifyRequest {

    @JsonProperty("destino")
    private String address;

    @JsonProperty("validoAte")
    private String validAt;

    @JsonCreator
    public TravelNotifyRequest(String address, String validAt) {
        this.address = address;
        this.validAt = validAt;
    }

    public static TravelNotifyRequest of(TravelNotice travelNotice){
        String address = travelNotice.getAddress().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String validAt = travelNotice.getEndOfTravel().format(formatter);
        return new TravelNotifyRequest(address, validAt);
    }
}
