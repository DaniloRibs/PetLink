package br.fai.faitec.petlink2026.configuration.security;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(String secret, long expirationMinutes) {

}
