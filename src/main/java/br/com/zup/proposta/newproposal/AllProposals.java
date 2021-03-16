package br.com.zup.proposta.newproposal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

interface AllProposals extends CrudRepository<Proposal, Long> {
    Optional<Proposal> findByDocument(String document);

    boolean existsByDocument(@NotBlank String document);
}
