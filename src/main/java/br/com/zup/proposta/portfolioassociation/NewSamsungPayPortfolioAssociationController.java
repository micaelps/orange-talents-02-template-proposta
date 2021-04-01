package br.com.zup.proposta.portfolioassociation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class NewSamsungPayPortfolioAssociationController extends TemplatePortfolioAssociation{

    @PostMapping("api/portfolio/association/samsung/{cardId}")
    public ResponseEntity<?> create(@PathVariable Long cardId, @RequestBody @Valid NewPortfolioAssociationRequest request) {
        return super.processNewAssociation(cardId, request);
    }

    @Override
    Portfolio getPortfolio() {
        return Portfolio.SAMSUNG;
    }
}
