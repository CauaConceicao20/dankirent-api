package com.dankirent.api.infrastructure.securtiy;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dankirent.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserDetailsImpl userDetails) {
        log.debug("Gerando token JWT para o usuário: {}", userDetails.getUsername());
        try {
            var token = JWT.create()
                    .withSubject(userDetails.getUsername())
                    .withIssuer("dankirent-api")
                    .withExpiresAt(generateExpirationDate())
                    .sign(Algorithm.HMAC256(secret));
            log.debug("Token JWT gerado com sucesso para o usuário: {}", userDetails.getUsername());
            return token;

        } catch (JWTCreationException exception) {
            log.warn("JWT inválido ou expirado");
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    public String getSubject(String token) {
        log.debug("Recuperando Subject do token JWT");
        try {
            var result = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("dankirent-api")
                    .build()
                    .verify(token)
                    .getSubject();

            log.debug("Subject recuperado com sucesso do token JWT");
            return result;

        } catch (JWTVerificationException exception){
            log.error("Erro ao verificar o token JWT", exception);
            throw new JWTVerificationException("Token inválido ou expirado", exception);
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
