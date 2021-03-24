package br.com.zup.proposta.blockedcard;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.card.CardVerificationClient;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

@RestController
public class BlockedCardController {

    final CardVerificationClient cardVerificationClient;
    final EntityManager entityManager;
    final AllBlockedCards allBlockedCards;
    final AllCards allCards;

    public BlockedCardController(CardVerificationClient cardVerificationClient, EntityManager entityManager, AllBlockedCards allBlockedCards, AllCards allCards) {
        this.cardVerificationClient = cardVerificationClient;
        this.entityManager = entityManager;
        this.allBlockedCards = allBlockedCards;
        this.allCards = allCards;
    }

    @PostMapping("/api/lock/cards/{idCard}")
    public ResponseEntity<?> LockCard(HttpServletRequest request, @PathVariable String idCard, @RequestHeader(value = "User-Agent") String userAgent) {
        String clientHostResolver = new ClientHostResolver(request).resolve();

        return allCards.findByExternalCardId(idCard)
                .map(card -> checkBlocked(card))
                .map(card -> new BlockedCard(idCard, clientHostResolver, userAgent))
                .map(allBlockedCards::save)
                .map(blockedCard -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Card checkBlocked(Card card) {
        try {
            cardVerificationClient.lock(card.getExternalCardId());
            return card;
        } catch (FeignException.UnprocessableEntity feue) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
