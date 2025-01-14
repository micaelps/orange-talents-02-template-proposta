package br.com.zup.proposta.blockedcard;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.common.CardVerificationClient;
import br.com.zup.proposta.common.ClientHostResolver;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
class BlockedCardController {

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
    public ResponseEntity<?> LockCard(HttpServletRequest httpServletRequest, @PathVariable Long idCard,
                                      @RequestHeader(value = "User-Agent") String userAgent, @RequestBody NewBlockedCardRequest request) {
        String clientHostResolver = new ClientHostResolver(httpServletRequest).resolve();

        return allCards.findById(idCard)
                .map(c-> block(c, request))
                .map(c-> new BlockedCard(c.getExternalCardId(), clientHostResolver, userAgent))
                .map(allBlockedCards::save)
                .map(x -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Card block(Card card, NewBlockedCardRequest request) {
        try {
            cardVerificationClient.lock(card.getExternalCardId(), request);
            card.block();
            return card;

        } catch (FeignException.UnprocessableEntity feue) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
