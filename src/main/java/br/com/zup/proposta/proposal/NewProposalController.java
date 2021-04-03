package br.com.zup.proposta.proposal;


import br.com.zup.proposta.common.EncryptDecrypt;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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

        String documentHash = Sha512DigestUtils.shaHex(request.document.getBytes(StandardCharsets.UTF_8));
        Optional<Proposal> byDocumentHash = allProposals.findByDocumentHash(documentHash);
        if(allProposals.existsByDocumentHash(documentHash)){
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
            String decrypt = EncryptDecrypt.decrypt(proposal.getDocument());
            FinancialVerificationRequest request = new FinancialVerificationRequest(decrypt,proposal.getName(), proposal.getId().toString());
            financialVerification.check(request);
            return  ProposalStatus.ELEGIBLE;

        }catch (FeignException.UnprocessableEntity feignException) {
            return ProposalStatus.NOT_ELIGIBLE;

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Broken error");
        }
    }

}
