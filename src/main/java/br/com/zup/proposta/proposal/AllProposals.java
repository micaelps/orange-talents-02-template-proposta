package br.com.zup.proposta.proposal;

import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public interface AllProposals extends CrudRepository<Proposal, Long> {
    Optional<Proposal> findByDocument(String document);

    boolean existsByDocument(@NotBlank String document);

    List<Proposal> findTop10ByStatusAndCardIsNull(ProposalStatus elegible);
}
