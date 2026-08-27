package br.fai.lds.projetolds2026.ports_and_adapters.port.service.crud;

public interface UpdateService<T> {

    boolean update(final int id, final T entity);
}
