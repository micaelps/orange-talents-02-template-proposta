package br.com.zup.proposta.card;

import br.com.zup.proposta.biometry.Biometry;
import br.com.zup.proposta.proposal.Proposal;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String external_card_id;

    private String holder;

    private LocalDateTime issuedOn;

    private BigDecimal limit_card;

    @OneToOne
    private Proposal proposal;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "card")
    private Set<Biometry> biometries = new HashSet<Biometry>();


    public Card(String external_card_id, String holder, LocalDateTime issuedOn, BigDecimal limit, Proposal proposal) {
        this.external_card_id = external_card_id;
        this.holder = holder;
        this.issuedOn = issuedOn;
        this.limit_card = limit;
        this.proposal = proposal;
    }

    @Deprecated
    public Card() {
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", external_card_id='" + external_card_id + '\'' +
                ", holder='" + holder + '\'' +
                ", issuedOn=" + issuedOn +
                ", limit_card=" + limit_card +
                ", proposal=" + proposal +
                '}';
    }

    public Card addBiometry(Biometry biometry) {
        this.biometries.add(biometry);
        return this;
    }
}
