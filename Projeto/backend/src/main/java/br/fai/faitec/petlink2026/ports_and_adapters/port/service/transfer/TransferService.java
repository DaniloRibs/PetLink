package br.fai.faitec.petlink2026.ports_and_adapters.port.service.transfer;

import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;

import java.util.List;


public interface TransferService {

    boolean requestTransfer(final int adoptionId, final String receiverEmail);

    boolean confirmTransfer(final int adoptionId, final int receiverId);

    boolean rejectTransfer(final int adoptionId, final int receiverId);

    List<AdoptionModel> findPendingByReceiverId(final int receiverId);
}
