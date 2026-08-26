package br.fai.lds.projetolds2026.ports_and_adapters.port.service.crud;

import br.fai.lds.projetolds2026.domain.UserModel;

public interface CreateService <T> {

    int create(final T entity);


}
