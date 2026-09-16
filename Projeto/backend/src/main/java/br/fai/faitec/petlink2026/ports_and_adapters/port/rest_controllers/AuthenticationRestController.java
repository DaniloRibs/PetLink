package br.fai.faitec.petlink2026.ports_and_adapters.port.rest_controllers;


import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.dto.user.AutenticationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationRestController {
    @PostMapping
    ResponseEntity<UserModel> authenticate(@RequestBody final AutenticationDto autenticationDto);
}
