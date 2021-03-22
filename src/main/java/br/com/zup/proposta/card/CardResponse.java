package br.com.zup.proposta.card;

import br.com.zup.proposta.biometry.Biometry;
import br.com.zup.proposta.biometry.NewBiometryResponse;
import br.com.zup.proposta.proposal.Proposal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CardResponse {

    @JsonProperty
    private String external_card_id;
    @JsonProperty
    private String holder;
    @JsonProperty
    private LocalDateTime issuedOn;
    @JsonProperty
    private BigDecimal limit_card;
    @JsonProperty
    private Proposal proposal;
    @JsonProperty
    @JsonManagedReference
    private Set<Biometry> biometrics = new HashSet<>();

    @JsonCreator
    public CardResponse(String external_card_id, String holder, LocalDateTime issuedOn, BigDecimal limit_card, Proposal proposal, Set<Biometry> biometrics) {
        this.external_card_id = external_card_id;
        this.holder = holder;
        this.issuedOn = issuedOn;
        this.limit_card = limit_card;
        this.proposal = proposal;
        this.biometrics = biometrics;
    }

    public static CardResponse of(Card card){
        return new CardResponse(card.getExternal_card_id(),
                card.getHolder(),
                card.getIssuedOn(),
                card.getLimit_card(),
                card.getProposal(),
                card.getBiometrics());
    }

}
