package br.com.zup.proposta.proposal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "financialVerification", url = "${financial.verification.url}")
public interface FinancialVerification {

    @RequestMapping(method = RequestMethod.POST, value = "/api/solicitacao")
    public FinancialVerificationResponse check(@RequestBody FinancialVerificationRequest request);

}


