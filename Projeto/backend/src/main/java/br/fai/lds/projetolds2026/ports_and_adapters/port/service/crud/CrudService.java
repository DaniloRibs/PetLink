package br.fai.lds.projetolds2026.ports_and_adapters.port.service.crud;

public interface CrudService <T> extends CreateService<T>, DeleteService, UpdateService<T>, FindService<T>{
}
