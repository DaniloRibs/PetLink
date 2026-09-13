package br.fai.faitec.petlink2026.ports_and_adapters.port.service.pet;

public interface UpdateOwnerService {

    boolean updateOwner(final int id, int oldOwner, int newOwner);
}
