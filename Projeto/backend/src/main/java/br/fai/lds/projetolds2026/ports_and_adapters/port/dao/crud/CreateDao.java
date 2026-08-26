package br.fai.lds.projetolds2026.ports_and_adapters.port.dao.crud;

import br.fai.lds.projetolds2026.domain.UserModel;

public interface CreateDao<T> {

    int add(final T entity);
}
