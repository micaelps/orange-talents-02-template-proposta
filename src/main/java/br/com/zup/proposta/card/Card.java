package br.com.zup.proposta.card;

import br.com.zup.proposta.biometry.Biometry;
import br.com.zup.proposta.biometry.NewBiometryResponse;
import br.com.zup.proposta.proposal.Proposal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY,mappedBy = "card")
    private Set<Biometry> biometrics = new HashSet<Biometry>();


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

    public Long getId() {
        return id;
    }

    public String getExternal_card_id() {
        return external_card_id;
    }

    public String getHolder() {
        return holder;
    }

    public LocalDateTime getIssuedOn() {
        return issuedOn;
    }

    public BigDecimal getLimit_card() {
        return limit_card;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public Set<Biometry> getBiometrics() {
        return biometrics;
    }
}

