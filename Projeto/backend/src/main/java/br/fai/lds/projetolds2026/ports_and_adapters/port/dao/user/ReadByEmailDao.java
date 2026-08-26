package br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user;

import br.fai.lds.projetolds2026.domain.UserModel;

public interface ReadByEmailDao {

    UserModel readByEmail(final String email);
}
