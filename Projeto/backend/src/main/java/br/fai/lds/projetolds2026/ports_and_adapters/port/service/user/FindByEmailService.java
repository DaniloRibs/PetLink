package br.fai.lds.projetolds2026.ports_and_adapters.port.service.user;

import br.fai.lds.projetolds2026.domain.user.UserModel;

public interface FindByEmailService {
    UserModel findByEmail(final String email);


}
