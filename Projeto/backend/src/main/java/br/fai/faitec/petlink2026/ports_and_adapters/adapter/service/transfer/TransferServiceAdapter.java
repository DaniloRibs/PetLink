package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.transfer;

import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;
import br.fai.faitec.petlink2026.domain.adoption.TransferStatus;
import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.adoption.AdoptionDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.PetService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.transfer.TransferService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransferServiceAdapter implements TransferService {

    @Autowired
    private AdoptionDao adoptionDao;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Override
    public boolean requestTransfer(final int adoptionId, final String receiverEmail) {
        if (isIdInvalid(adoptionId) || receiverEmail == null || receiverEmail.isBlank()) {
            return false;
        }

        final AdoptionModel adoption = adoptionDao.readyById(adoptionId);

        if (adoption == null) {
            return false;
        }

        if (adoption.getTransferStatus() == TransferStatus.ACCEPTED
                || adoption.getTransferStatus() == TransferStatus.PENDING) {
            return false;
        }

        final UserModel receiver = userService.findByEmail(receiverEmail.trim());

        if (receiver == null || receiver.getId() == adoption.getOwnerId()) {
            return false;
        }

        final PetModel pet = petService.findById(adoption.getPetId());

        if (pet == null || pet.getOwnerId() != adoption.getOwnerId()) {
            return false;
        }

        adoption.setReceiverId(receiver.getId());
        adoption.setTransferStatus(TransferStatus.PENDING);
        adoption.setAdopted(true);

        adoptionDao.updateInformation(adoptionId, adoption);

        return true;
    }

    @Override
    public boolean confirmTransfer(final int adoptionId, final int receiverId) {
        final AdoptionModel adoption = findPendingTransfer(adoptionId, receiverId);

        if (adoption == null) {
            return false;
        }

        final boolean transferred = petService.updateOwner(adoption.getPetId(), adoption.getOwnerId(), receiverId);

        if (!transferred) {
            return false;
        }

        adoption.setTransferStatus(TransferStatus.ACCEPTED);
        adoptionDao.updateInformation(adoptionId, adoption);

        return true;
    }

    @Override
    public boolean rejectTransfer(final int adoptionId, final int receiverId) {
        final AdoptionModel adoption = findPendingTransfer(adoptionId, receiverId);

        if (adoption == null) {
            return false;
        }

        adoption.setTransferStatus(TransferStatus.REJECTED);
        adoption.setAdopted(false);

        adoptionDao.updateInformation(adoptionId, adoption);

        return true;
    }

    @Override
    public List<AdoptionModel> findPendingByReceiverId(final int receiverId) {
        final List<AdoptionModel> pending = new ArrayList<>();

        if (isIdInvalid(receiverId)) {
            return pending;
        }

        for (final AdoptionModel adoption : adoptionDao.readAll()) {
            if (adoption.getTransferStatus() == TransferStatus.PENDING && adoption.getReceiverId() == receiverId) {
                pending.add(adoption);
            }
        }

        return pending;
    }

    private AdoptionModel findPendingTransfer(final int adoptionId, final int receiverId) {
        if (isIdInvalid(adoptionId) || isIdInvalid(receiverId)) {
            return null;
        }

        final AdoptionModel adoption = adoptionDao.readyById(adoptionId);

        if (adoption == null || adoption.getTransferStatus() != TransferStatus.PENDING) {
            return null;
        }

        if (adoption.getReceiverId() != receiverId) {
            return null;
        }

        return adoption;
    }

    private boolean isIdInvalid(final int id) {
        return id <= 0;
    }
}
