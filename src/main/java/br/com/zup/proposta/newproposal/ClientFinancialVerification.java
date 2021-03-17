package br.com.zup.proposta.newproposal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "jplaceholder", url = "${financial.verification.url}")
public interface ClientFinancialVerification {

    @RequestMapping(method = RequestMethod.POST, value = "/api/solicitacao")
    public FinancialVerificationResponse ClientFinancialVerification(@RequestBody FinancialVerificationRequest request);

}


