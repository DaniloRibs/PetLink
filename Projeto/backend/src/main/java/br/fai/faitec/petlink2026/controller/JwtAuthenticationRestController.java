package br.fai.faitec.petlink2026.controller;

import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.dto.JwtTokenDto;
import br.fai.faitec.petlink2026.dto.user.AutenticationDto;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.security.jwt.JwtService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.security.AuthenticationService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("jwt")
@RequestMapping("/authenticate")
public class JwtAuthenticationRestController {


    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationRestController(AuthenticationService authenticationService, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<JwtTokenDto> authenticate(@RequestBody AutenticationDto autenticationDto) {
        UserModel authenticatedUser = authenticationService.authenticate(autenticationDto.getEmail(), autenticationDto.getPassword());

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticatedUser.getEmail());

        final String jwt = jwtService.generateTokenClaims(
                userDetails,
                authenticatedUser.getFullname(),
                authenticatedUser.getAccountType(),
                authenticatedUser.getEmail()
        );

        if (jwt == null || jwt.isEmpty()) {
            throw new InternalError("Token invalido");
        }

        final JwtTokenDto jwtTokenDto = new JwtTokenDto(jwt);

        return ResponseEntity.ok(jwtTokenDto);
    }
}
