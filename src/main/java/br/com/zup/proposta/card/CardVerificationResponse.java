package br.com.zup.proposta.card;

import br.com.zup.proposta.proposal.AllProposals;
import br.com.zup.proposta.proposal.Proposal;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class CardVerificationResponse {

    @JsonProperty("id")
    private final String external_card_id;

    @JsonProperty("titular")
    private final String holder;

    @JsonProperty("emitidoEm")
    private final LocalDateTime issuedOn;

    @JsonProperty("limite")
    private final BigDecimal limit;

    @JsonProperty("idProposta")
    private final Long idProposal;

    CardVerificationResponse(String external_card_id, String holder, LocalDateTime issuedOn, BigDecimal limit, Long idProposal) {
        this.external_card_id = external_card_id;
        this.holder = holder;
        this.issuedOn = issuedOn;
        this.limit = limit;
        this.idProposal = idProposal;
    }

    @Override
    public String toString() {
        return "CardVerificationResponse{" +
                "external_card_id='" + external_card_id + '\'' +
                ", holder='" + holder + '\'' +
                ", issuedOn=" + issuedOn +
                ", limit=" + limit +
                ", idProposal='" + idProposal + '\'' +
                '}';
    }

    Card toCard(Proposal proposal){
        return  new Card(external_card_id, holder, issuedOn, limit, proposal);
    }
}
