package br.com.zup.proposta.card;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "cardVerification", url = "${card.verification.url}")
public interface CardVerificationClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartoes")
    public CardVerificationResponse register(@RequestBody CardVerificationRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartoes/{cardId}")
    public Map<String, String> lock(@PathVariable String cardId);
}
