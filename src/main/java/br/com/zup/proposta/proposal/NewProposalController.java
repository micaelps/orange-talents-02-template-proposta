package br.com.zup.proposta.proposal;


import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/proposals")
public class NewProposalController {

    final AllProposals allProposals;
    final FinancialVerification financialVerification;
    final Tracer tracer;


    public NewProposalController(AllProposals allProposals, FinancialVerification financialVerification, Tracer tracer) {
        this.allProposals = allProposals;
        this.financialVerification = financialVerification;
        this.tracer = tracer;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid NewProposalRequest request) {
        Span span = tracer.activeSpan();
        span.setBaggageItem("proposalRequest.email", request.email);

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

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long proposalId) {
        return allProposals.findById(proposalId)
                .map((x) -> ResponseEntity.ok(NewProposalResponse.of(x)))
                .orElseGet(() -> ResponseEntity.notFound().build());
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

}
