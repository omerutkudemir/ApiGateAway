package com.gateaway.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // OAuth2 kimlik doğrulama endpoint'lerini ve kullanıcı kayıt endpoint'ini herkese açın
                        .pathMatchers(
                                "/oauth2/**",
                                "/login",
                                "/actuator/**",
                                "/.well-known/**",
                                "/jwks",
                                "/api/users/register", // Kullanıcı kayıt endpoint'i hala public olmalı
                                "/api/users/login"
                        ).permitAll()
                        // Diğer tüm istekler için kimlik doğrulama gereksinimi
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())); // Gelen JWT token'ları doğrula

        return http.build();
    }
}