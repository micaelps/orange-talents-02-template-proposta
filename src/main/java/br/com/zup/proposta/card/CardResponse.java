package br.com.zup.proposta.card;

import br.com.zup.proposta.proposal.NewProposalResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private NewProposalResponse proposal;

    @JsonCreator
    public CardResponse(String external_card_id, String holder, LocalDateTime issuedOn, BigDecimal limit_card, NewProposalResponse proposal) {
        this.external_card_id = external_card_id;
        this.holder = holder;
        this.issuedOn = issuedOn;
        this.limit_card = limit_card;
        this.proposal = proposal;
    }

    public static CardResponse of(Card card){
        return new CardResponse(card.getExternalCardId(),
                card.getHolder(),
                card.getIssuedOn(),
                card.getLimitCard(),
               NewProposalResponse.of(card.getProposal()));
    }

}
