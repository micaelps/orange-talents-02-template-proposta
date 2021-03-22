package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.util.Optional;

@RestController
public class NewBiometryController {

    @Autowired
    private AllBiometrics allBiometrics;

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    @RequestMapping("api/biometrics/{id}")
    public ResponseEntity<?> save(@RequestBody NewBiometryRequest request, @PathVariable("id") Long cardId) {
       return Optional.ofNullable(manager.find(Card.class, cardId))
                .map(request::toModel)
                .map(allBiometrics::save)
                .map(biometry -> ResponseEntity.ok(buildUri(biometry)))
                .orElseGet((() -> ResponseEntity.notFound().build()));
    }

    private URI buildUri(Biometry biometry){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(biometry.getId()).toUri();
    }
}
