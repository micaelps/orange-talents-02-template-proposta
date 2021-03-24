package br.com.zup.proposta.blockedcard;

import javax.servlet.http.HttpServletRequest;


public class ClientHostResolver {

    private final HttpServletRequest request;

    public ClientHostResolver(HttpServletRequest request) {
        this.request = request;
    }

    public String resolve() {

        String xRealIp = request.getHeader("X-Real-IP");
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String remoteAddr = request.getRemoteAddr();

        if (xRealIp != null)
            return xRealIp;

        return firstNonNull(xForwardedFor, remoteAddr);
    }

    private String firstNonNull(String first, String second) {
        if (first != null) {
            return first;
        } else if (second != null) {
            return second;
        } else {
            throw new NullPointerException("Both parameters are null");
        }
    }
}