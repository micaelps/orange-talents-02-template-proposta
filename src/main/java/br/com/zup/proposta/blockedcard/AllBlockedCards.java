package br.com.zup.proposta.blockedcard;

import org.springframework.data.repository.CrudRepository;

interface AllBlockedCards extends CrudRepository<BlockedCard, Long> {
}
