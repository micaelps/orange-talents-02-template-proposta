package br.com.zup.proposta.proposal.feignClients;

public class CardVerificationResponse {

    private String id;
    private String titular;

    public CardVerificationResponse(String id, String titular) {
        this.id = id;
        this.titular = titular;
    }


    public String getId() {
        return id;
    }

    public String getTitular() {
        return titular;
    }

    @Override
    public String toString() {
        return "CardVerificationResponse{" +
                "id='" + id + '\'' +
                ", titular='" + titular + '\'' +
                '}';
    }
}
