package br.fai.lds.projetolds2026.ports_and_adapters.port.dao.crud;

public interface UpdateDao<T> {

    void updateInformation(final int id, final T entity);
}
