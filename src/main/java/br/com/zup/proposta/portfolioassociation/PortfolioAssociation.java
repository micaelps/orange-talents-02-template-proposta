package br.com.zup.proposta.portfolioassociation;


import br.com.zup.proposta.card.Card;

import javax.persistence.*;

@Entity
public class PortfolioAssociation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @ManyToOne
    private Card card;

    @Enumerated(EnumType.STRING)
    private Portfolio portfolio;

    public PortfolioAssociation(String email, Card card, Portfolio portfolio) {
        this.email = email;
        this.card = card;
        this.portfolio = portfolio;
    }

    @Deprecated
    public PortfolioAssociation() {
    }
}
