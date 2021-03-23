package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.card.CardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class NewBiometryController {

    @Autowired
    private AllBiometrics allBiometrics;

    @PersistenceContext
    EntityManager manager;

    @Transactional
    @PostMapping("api/biometrics/{id}")
    public ResponseEntity<?> save(@RequestBody @Valid NewBiometryRequest request, @PathVariable("id") Long cardId, UriComponentsBuilder uriBuilder) {
        Card aa = manager.find(Card.class, cardId);
        System.out.println(">>>>>>>>>>>>>>>>>>>>> "+ aa);
        return Optional.ofNullable(manager.find(Card.class, cardId))
                .map(request::toModel)
                .map(allBiometrics::save)
                .map(biometry -> ResponseEntity.created(buildUri(biometry, uriBuilder)).build())
                .orElseGet((() -> ResponseEntity.notFound().build()));
    }

    @GetMapping("api/biometrics/{biometryId}")
    public ResponseEntity<NewBiometryResponse> getById(@PathVariable Long biometryId){
        return  allBiometrics.findById(biometryId)
                .map(biometry-> ResponseEntity.ok(NewBiometryResponse.of(biometry)))
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    private URI buildUri(Biometry biometry, UriComponentsBuilder uriBuilder){
        return uriBuilder
                .path("/api/biometrics/{id}")
                .buildAndExpand(biometry.getId()).toUri();
    }
}
