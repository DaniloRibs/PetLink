package br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user;

public interface UpdatePasswordDao {

    boolean updatePassword(final int id, final String password);
}
