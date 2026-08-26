package br.fai.lds.projetolds2026.ports_and_adapters.port.service.user;

public interface UpdatePasswordService {

    boolean updatePassword(final int id, String oldPassword, String newPassword);
}
