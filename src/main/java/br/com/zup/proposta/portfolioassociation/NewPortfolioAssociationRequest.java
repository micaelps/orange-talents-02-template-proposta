package br.com.zup.proposta.portfolioassociation;

import br.com.zup.proposta.card.Card;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;

public class NewPortfolioAssociationRequest {

    @JsonProperty
    @Email
    final String email;

    @JsonCreator
    public NewPortfolioAssociationRequest(String email) {
        this.email = email;
    }


    public AssociationCardClientRequest toClientRequest(Portfolio portfolio) {
            return new AssociationCardClientRequest(this.email, portfolio.toString());
    }

    public PortfolioAssociation toPortfolioAssociation(Card card, Portfolio portfolio){
        return  new PortfolioAssociation(this.email, card, portfolio);
    }

}
