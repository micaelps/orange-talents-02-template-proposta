package br.com.zup.proposta.travelnotice;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.common.ClientHostResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class NewTravelNoticeController {

    final AllCards allCards;
    final AllTravelNotices allTravelNotices;

    public NewTravelNoticeController(AllCards allCards, AllTravelNotices allTravelNotices) {
        this.allCards = allCards;
        this.allTravelNotices = allTravelNotices;
    }

    @PostMapping("/api/travel/notice/{cardId}")
    public ResponseEntity<?> save(@RequestBody @Valid NewTravelNoticeRequest request, @PathVariable Long cardId,
                                  HttpServletRequest httpServletRequest, @RequestHeader(value = "User-Agent") String userAgent) {

        String clientHostResolver = new ClientHostResolver(httpServletRequest).resolve();
        return allCards.findById(cardId)
                .map(c -> request.toTravelNotice(clientHostResolver, userAgent))
                .map(tn -> allTravelNotices.save(tn))
                .map(r -> ResponseEntity.ok().build())
                .orElseGet(()->ResponseEntity.notFound().build());

    }
}
