package br.com.zup.proposta.biometry;

import org.springframework.data.repository.CrudRepository;

interface AllBiometrics extends CrudRepository<Biometry, Long> {
}
