package br.fai.faitec.petlink2026.controller;

import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.dto.user.AutenticationDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.rest_controllers.AuthenticationRestController;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class BasicAuthenticateRestController implements AuthenticationRestController {

    private final AuthenticationService authenticationService;

    public BasicAuthenticateRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping
    @Override
    public ResponseEntity<UserModel> authenticate(@RequestBody AutenticationDto autenticationDto) {
        UserModel authenticatedUser = authenticationService.authenticate(autenticationDto.getEmail(), autenticationDto.getPassword());

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authenticatedUser);

    }
}
