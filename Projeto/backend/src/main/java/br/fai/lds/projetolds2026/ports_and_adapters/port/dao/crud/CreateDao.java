package br.fai.lds.projetolds2026.ports_and_adapters.port.dao.crud;

public interface CreateDao<T> {

    int add(final T entity);
}
