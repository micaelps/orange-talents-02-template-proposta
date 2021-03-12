package br.com.zup.proposta.newproposal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static java.util.Objects.*;

@RestController
@RequestMapping("/api/proposals")
class NewProposalController {

    @Autowired
    private AllProposals allProposals;

    @Transactional
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid NewProposalRequest request) {

        if(request.existsProposal(allProposals, request.document)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Proposal proposal = request.toProposal();
        allProposals.save(proposal);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(proposal.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long proposalId){
        Optional<Proposal> proposal = allProposals.findById(proposalId);

        if(proposal.isPresent()) {
            return ResponseEntity.ok(NewProposalResponse.of(proposal.get()));
        }

        return ResponseEntity.notFound().build();
    }
}
