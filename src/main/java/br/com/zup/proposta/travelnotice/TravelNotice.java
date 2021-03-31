package br.com.zup.proposta.travelnotice;

import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.common.Address;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TravelNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Address address;

    private LocalDate endOfTravel;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String clientIp;

    private String userAgent;

    @ManyToOne
    private Card card;


    public TravelNotice(Address address, LocalDate endOfTravel, String clientIp, String userAgent, Card card) {
        this.address = address;
        this.endOfTravel = endOfTravel;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
        this.card = card;
    }

    public Card getCard() {
        return this.card;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDate getEndOfTravel() {
        return endOfTravel;
    }
}
