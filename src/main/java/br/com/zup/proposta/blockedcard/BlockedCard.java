package br.com.zup.proposta.blockedcard;


import javax.persistence.*;

@Entity
public class BlockedCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;

    private String clientIp;

    private String userAgent;


    public BlockedCard(String externalId, String clientIp, String userAgent) {
        this.externalId = externalId;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }

    public String getExternalId() {
        return externalId;
    }
}
