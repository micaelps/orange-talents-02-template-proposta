package br.com.zup.proposta.config.security;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth", url = "${auth.url}")
interface AuthenticationClient{

    @PostMapping
    LoginResponse auth(MultiValueMap<String, String> request);
}