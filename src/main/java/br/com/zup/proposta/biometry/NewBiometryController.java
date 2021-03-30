package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
class NewBiometryController {

    final AllBiometrics allBiometrics;
    final EntityManager manager;

    public NewBiometryController(AllBiometrics allBiometrics, EntityManager manager) {
        this.allBiometrics = allBiometrics;
        this.manager = manager;
    }

    @Transactional
    @PostMapping("api/biometrics/{id}")
    public ResponseEntity<?> save(@RequestBody @Valid NewBiometryRequest request, @PathVariable("id") Long cardId, UriComponentsBuilder uriBuilder) {
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
