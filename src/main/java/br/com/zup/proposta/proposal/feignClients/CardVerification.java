package br.com.zup.proposta.proposal.feignClients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "cardVerification", url = "${card.verification.url}")
public interface CardVerification {

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartoes")
    public CardVerificationResponse verify(@RequestBody CardVerificationRequest request);
}
