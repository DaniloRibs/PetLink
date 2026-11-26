package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.security.jwt;

import br.fai.faitec.petlink2026.configuration.security.JwtProperties;
import br.fai.faitec.petlink2026.domain.user.AccountType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Profile("jwt")
@Component
public class JwtService {

    private final Key signingKey;
    private final Duration expiration;

    public JwtService(JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secret()));
        this.expiration = Duration.ofMinutes(properties.expirationMinutes());
    }

    public String getEmailFromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    public Date getExpirationFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public boolean tokenExpired(String token) {
        final Date expirationDate = getExpirationFromToken(token);
        return expirationDate.before(new Date());
    }

    public boolean validateToken(
            String token,
            UserDetails userDetails
    ) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !tokenExpired(token));
    }

    public String generateTokenClaims(UserDetails userDetails, String fullname, AccountType role, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("fullname", fullname);
        claims.put("role", role.name());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        final Date now = new Date();
        final Date expiresAt = new Date(now.getTime() + expiration.toMillis());

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(signingKey, SignatureAlgorithm.HS256).compact();
    }

}
