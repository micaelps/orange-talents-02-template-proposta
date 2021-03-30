package br.com.zup.proposta.travelnotice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

interface AllTravelNotices extends CrudRepository<TravelNotice, Long> {
}
