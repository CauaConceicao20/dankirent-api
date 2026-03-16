package com.dankirent.api.infrastructure.cookie;

import com.dankirent.api.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public ResponseCookie setAccessTokenInCookie(String token, HttpServletResponse response) {
        log.debug("Adicionando cookie de acesso ao response");
        ResponseCookie cookie = ResponseCookie.from("sessionToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Strict")
                .build();

        log.debug("Cookie de acesso adicionado");
        return cookie;
    }

    public ResponseCookie removeAccessTokenInCookie(HttpServletResponse response) {
        log.debug("Removendo cookie de acesso ao response");
        ResponseCookie cookie = ResponseCookie.from("sessionToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        log.debug("Cookie de acesso removido");
        return cookie;
    }
}
