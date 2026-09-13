package br.fai.faitec.petlink2026.ports_and_adapters.port.dao.crud;

public interface UpdateDao<T> {

    void updateInformation(final int id, final T entity);
}
