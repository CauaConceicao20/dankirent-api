package com.dankirent.api.service;

import com.dankirent.api.infrastructure.securtiy.JwtService;
import com.dankirent.api.infrastructure.securtiy.UserDetailsImpl;
import com.dankirent.api.model.auth.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String authenticate(LoginRequestDto dataLogin) {
        log.debug("Autenticando usuário: email={}", dataLogin.email());
        var loginPassword = new UsernamePasswordAuthenticationToken(dataLogin.email(), dataLogin.password());
        var auth = this.authenticationManager.authenticate(loginPassword);

        return jwtService.generateToken((UserDetailsImpl) auth.getPrincipal());
    }
}
