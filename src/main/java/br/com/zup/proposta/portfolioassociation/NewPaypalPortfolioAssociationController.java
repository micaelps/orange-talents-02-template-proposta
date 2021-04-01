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
class NewPaypalPortfolioAssociationController extends TemplatePortfolioAssociation {

    @PostMapping("api/portfolio/association/paypal/{cardId}")
    public ResponseEntity<?> create(@PathVariable Long cardId, @RequestBody @Valid NewPortfolioAssociationRequest request) {
        return super.processNewAssociation(cardId, request);
    }

    @Override
    Portfolio getPortfolio() {
        return Portfolio.PAYPAL;
    }
}
