package br.com.zup.proposta.newproposal;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface AllProposals extends CrudRepository<Proposal, Long> {
    Optional<Proposal> findByDocument(String document);
}
