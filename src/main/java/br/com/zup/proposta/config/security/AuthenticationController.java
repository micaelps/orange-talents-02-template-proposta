package br.com.zup.proposta.config.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
class AuthenticationController {

    @Autowired
    AuthenticationClient authenticationClient;

    @PostMapping
    public LoginResponse auth(@RequestBody LoginRequest request) {

        MultiValueMap<String, String> dataUrlEncode = new LinkedMultiValueMap<String, String>();
        dataUrlEncode.add("grant_type", request.grantType);
        dataUrlEncode.add("username", request.username);
        dataUrlEncode.add("password", request.password);
        dataUrlEncode.add("client_id", request.clientId);
        dataUrlEncode.add("scope", request.scope);

        return authenticationClient.auth(dataUrlEncode);
    }
}
