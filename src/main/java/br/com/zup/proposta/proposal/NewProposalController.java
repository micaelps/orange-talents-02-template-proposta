package br.com.zup.proposta.proposal;


import br.com.zup.proposta.proposal.feignClients.CardVerification;
import br.com.zup.proposta.proposal.feignClients.*;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/proposals")
class NewProposalController {

    @Autowired
    private AllProposals allProposals;

    @Autowired
    private FinancialVerification financialVerification;

    @Autowired
    private CardVerification cardVerification;

    @Transactional
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid NewProposalRequest request) {

        if(allProposals.existsByDocument(request.document)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Proposal proposal = request.toProposal();
        allProposals.save(proposal);
        ProposalStatus statusProcessed = processStatus(proposal);
        proposal.updateStatus(statusProcessed);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(proposal.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    private ProposalStatus processStatus(Proposal proposal) {
        try {
            FinancialVerificationRequest request = new FinancialVerificationRequest(proposal.getDocument(),proposal.getName(), proposal.getId().toString());
            financialVerification.check(request);
            return  ProposalStatus.ELEGIBLE;

        }catch (FeignException.UnprocessableEntity feignException) {
            return ProposalStatus.NOT_ELIGIBLE;

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Broken error");
        }
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
