package br.fai.faitec.petlink2026.ports_and_adapters.port.dao.crud;

public interface CreateDao<T> {

    int add(final T entity);
}
