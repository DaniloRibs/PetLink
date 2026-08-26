package br.fai.lds.projetolds2026.ports_and_adapters.port.service.crud;

import br.fai.lds.projetolds2026.domain.UserModel;

public interface UpdateService<T> {

    boolean update(final int id, final T entity);
}
