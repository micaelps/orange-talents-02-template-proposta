package br.com.zup.proposta.travelnotice;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.common.CardVerificationClient;
import br.com.zup.proposta.common.ClientHostResolver;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NewTravelNoticeController {

    final AllCards allCards;
    final AllTravelNotices allTravelNotices;
    final CardVerificationClient cardVerificationClient;

    public NewTravelNoticeController(AllCards allCards, AllTravelNotices allTravelNotices, CardVerificationClient cardVerificationClient) {
        this.allCards = allCards;
        this.allTravelNotices = allTravelNotices;
        this.cardVerificationClient = cardVerificationClient;
    }

    @PostMapping("/api/travel/notice/{cardId}")
    public ResponseEntity<?> save(@RequestBody @Valid NewTravelNoticeRequest request, @PathVariable Long cardId,
                                  HttpServletRequest httpServletRequest, @RequestHeader(value = "User-Agent") String userAgent) {

        String clientHostResolver = new ClientHostResolver(httpServletRequest).resolve();
        return allCards.findById(cardId)
                .map(c -> request.toTravelNotice(clientHostResolver, userAgent, c))
                .map(tn -> notify(tn))
                .map(tn -> allTravelNotices.save(tn))
                .map(r -> ResponseEntity.ok().build())
                .orElseGet(()->ResponseEntity.notFound().build());
    }


    private TravelNotice notify(TravelNotice travelNotice) {
        try {

            String externalCardId = travelNotice.getCard().getExternalCardId();
            Map<String, String> stringStringMap = cardVerificationClient.notifyTravel(externalCardId, TravelNotifyRequest.of(travelNotice));
            return travelNotice;

        } catch (FeignException.UnprocessableEntity feue) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch (FeignException.FeignServerException feignServerException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
