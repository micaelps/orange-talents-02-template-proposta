package br.com.zup.proposta.travelnotice;

import br.com.zup.proposta.common.Address;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TravelNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Address to;

    private LocalDate endOfTravel;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String clientIp;

    private String userAgent;


    public TravelNotice(Address to, LocalDate endOfTravel, String clientIp, String userAgent) {
        this.to = to;
        this.endOfTravel = endOfTravel;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }
}
