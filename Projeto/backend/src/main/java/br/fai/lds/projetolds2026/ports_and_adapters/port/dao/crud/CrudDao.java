package br.fai.lds.projetolds2026.ports_and_adapters.port.dao.crud;

public interface CrudDao<T> extends CreateDao<T>, DeleteDao,ReadDao<T>, UpdateDao<T> {
}
