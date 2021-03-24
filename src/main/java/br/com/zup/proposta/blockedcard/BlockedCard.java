package br.com.zup.proposta.blockedcard;


import javax.persistence.*;

@Entity
public class BlockedCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externaId;

    private String clientIp;

    private String userAgent;


    public BlockedCard(String externaId, String clientIp, String userAgent) {
        this.externaId = externaId;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }

    public String getExternaId() {
        return externaId;
    }
}
