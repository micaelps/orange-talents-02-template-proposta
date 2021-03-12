package br.com.zup.proposta.newproposal;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.*;

@RestController
@RequestMapping("/api/proposals")
class NewProposalController {

    @PersistenceContext
    EntityManager manager;

    @Transactional
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid NewProposalRequest request) {

        Proposal proposal = request.toProposal();
        manager.persist(proposal);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(proposal.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long proposalId){
        Proposal proposal = manager.find(Proposal.class, proposalId);

        if(isNull(proposal)){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(NewProposalResponse.of(proposal));
    }
}
