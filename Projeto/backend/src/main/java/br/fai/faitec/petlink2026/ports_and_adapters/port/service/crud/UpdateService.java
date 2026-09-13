package br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud;

public interface UpdateService<T> {

    boolean update(final int id, final T entity);
}
