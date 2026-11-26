package br.fai.faitec.petlink2026.configuration.security;


import br.fai.faitec.petlink2026.domain.user.AccountType;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.security.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Profile("jwt")
@Configuration
public class JwtSecurityConfiguration extends BasicSecurityConfiguration {

    private final JwtRequestFilter jwtRequestFilter;


    public JwtSecurityConfiguration(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // login
                        .requestMatchers("/authenticate").permitAll()

                        // cadastro de usuario (ainda nao existe token)
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()

                        // demais endpoints da API: qualquer tipo de conta autenticada
                        .requestMatchers("/api/**").hasAnyRole(AccountType.PERSON.name(), AccountType.ENTERPRISE.name())

                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }
}
