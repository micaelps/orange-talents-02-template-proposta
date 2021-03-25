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

    private String externalCardId;

    private String holder;

    private LocalDateTime issuedOn;

    private BigDecimal limitCard;

    @OneToOne
    private Proposal proposal;

    @Column(nullable = false)
    private Boolean block = false;

    @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY,mappedBy = "card")
    private Set<Biometry> biometrics = new HashSet<Biometry>();


    public Card(String externalCardId, String holder, LocalDateTime issuedOn, BigDecimal limit, Proposal proposal) {
        this.externalCardId = externalCardId;
        this.holder = holder;
        this.issuedOn = issuedOn;
        this.limitCard = limit;
        this.proposal = proposal;
    }

    @Deprecated
    public Card() {
    }

    public Long getId() {
        return id;
    }

    public String getExternalCardId() {
        return externalCardId;
    }

    public String getHolder() {
        return holder;
    }

    public LocalDateTime getIssuedOn() {
        return issuedOn;
    }

    public BigDecimal getLimitCard() {
        return limitCard;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public Set<Biometry> getBiometrics() {
        return biometrics;
    }

    public Card block() {
        this.block = true;
        return this;
    }

    public Boolean isBlocked() {
        return block;
    }
}

