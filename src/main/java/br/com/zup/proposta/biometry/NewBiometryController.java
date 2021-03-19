package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RestController
public class NewBiometryController {

    @PersistenceContext
    private EntityManager manager;


    @Transactional
    @RequestMapping("api/biometrics/{id}")
    public ResponseEntity<?> save(@RequestBody NewBiometryRequest request, @PathVariable("id") Long cardId) {
        return Optional.ofNullable(manager.find(Card.class, cardId))
                .map(card -> card.addBiometry(request.toModel(card)))
                .map(card -> manager.merge(card))
                .map(card -> ResponseEntity.ok().build())
                .orElseGet((() -> ResponseEntity.notFound().build()));
    }
}
