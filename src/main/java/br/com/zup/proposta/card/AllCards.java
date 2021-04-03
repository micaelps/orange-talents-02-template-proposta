package br.com.zup.proposta.card;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AllCards extends CrudRepository<Card, Long> {
}
