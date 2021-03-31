package br.com.zup.proposta.portfolioassociation;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.common.CardVerificationClient;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Map;

@RestController
class NewPaypalPortfolioAssociationController {

    final AllPortfolioAssociations allPortfolioAssociations;
    final AllCards allCards;
    final CardVerificationClient client;

    NewPaypalPortfolioAssociationController(AllPortfolioAssociations allPortfolioAssociations, AllCards allCards, CardVerificationClient client) {
        this.allPortfolioAssociations = allPortfolioAssociations;
        this.allCards = allCards;
        this.client = client;
    }

    @PostMapping("api/portfolio/association/{cardId}")
    public ResponseEntity<?> create(@PathVariable Long cardId, @RequestBody @Valid NewPortfolioAssociationRequest request) {
        return allCards.findById(cardId)
                .map(c -> associate(c, request))
                .map(allPortfolioAssociations::save)
                .map(pa -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    private PortfolioAssociation associate(Card card, NewPortfolioAssociationRequest request) {
        try {
            client.associate(card.getExternalCardId(), request.toClientRequest(Portfolio.PAYPAL));
            return request.toPortfolioAssociation(card, Portfolio.PAYPAL);

        } catch (FeignException.UnprocessableEntity feue) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
