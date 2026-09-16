package br.fai.faitec.petlink2026.ports_and_adapters.port.service.security;

import br.fai.faitec.petlink2026.domain.user.UserModel;

public interface AuthenticationService {

    UserModel authenticate(final String email, final String password);
}
