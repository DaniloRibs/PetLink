package br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user;

import br.fai.faitec.petlink2026.domain.user.UserModel;

public interface ReadByEmailDao {

    UserModel readByEmail(final String email);
}
