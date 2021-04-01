package br.com.zup.proposta.portfolioassociation;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.common.CardVerificationClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
abstract class TemplatePortfolioAssociation {

    @Autowired
    private AllPortfolioAssociations allPortfolioAssociations;

    @Autowired
    private AllCards allCards;

    @Autowired
    private CardVerificationClient client;

    public TemplatePortfolioAssociation() {
    }

    private PortfolioAssociation associate(Card card, NewPortfolioAssociationRequest request) {
        try {
            client.associate(card.getExternalCardId(), request.toClientRequest(getPortfolio()));
            return request.toPortfolioAssociation(card, getPortfolio());

        } catch (FeignException.UnprocessableEntity feue) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    abstract Portfolio getPortfolio();

    ResponseEntity<?> processNewAssociation(Long cardId, NewPortfolioAssociationRequest request) {
        return allCards.findById(cardId)
                .map(c -> associate(c, request))
                .map(allPortfolioAssociations::save)
                .map(pa -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
