package br.com.zup.proposta.travelnotice;

import br.com.zup.proposta.card.AllCards;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> save(@RequestBody @Valid NewTravelNoticeRequest request, @PathVariable Long cardId) {

        return allCards.findById(cardId)
                .map(c -> request.toTravelNotice())
                .map(tn -> allTravelNotices.save(tn))
                .map(r -> ResponseEntity.ok().build())
                .orElseGet(()->ResponseEntity.notFound().build());

    }
}
