package br.fai.lds.projetolds2026.ports_and_adapters.port.service.crud;

import java.util.List;

public interface FindService<T> {

    T findById(final int id);

    List<T> findALl();
}
