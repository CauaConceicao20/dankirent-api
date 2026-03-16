package com.dankirent.api.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Executando filtro JWT");
        var token = recoverToken(request);

        if (token != null) {
            log.debug("Token JWT válido para o subject");

            var login = jwtService.getSubject(token);
            var user = userDetailsService.loadUserByUsername(login);
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("SecurityContext autenticado com sucesso");

        }
        filterChain.doFilter(request, response);

    }

    private String recoverToken(HttpServletRequest request) {
        log.debug("Recuperando token JWT do cookie");
        Cookie cookie = WebUtils.getCookie(request, "sessionToken");

        if (cookie != null) {
            log.debug("Token JWT recuperado com sucesso");
            return cookie.getValue();
        }
        log.warn("Nenhum token JWT encontrado no cookie");
        return null;
    }
}
