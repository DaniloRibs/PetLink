package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.adoption;

import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;
import br.fai.faitec.petlink2026.domain.adoption.TransferStatus;
import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.adoption.AdoptionDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.PetDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.adoption.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class AdoptionServiceAdapter implements AdoptionService {

    @Autowired
    private AdoptionDao adoptionDao;
    @Autowired
    private PetDao petDao;
    @Autowired
    private UserDao userDao;

    @Override
    public int create(AdoptionModel adoptionModel) {

        if (adoptionModel == null) {
            return 0;
        }

        if (adoptionModel.getDescription() == null || adoptionModel.getDescription().isEmpty()) {
            return 0;
        }

        PetModel pet = petDao.readyById(adoptionModel.getPetId());

        if (pet == null) {
            return 0;
        }

        UserModel owner = userDao.readyById(adoptionModel.getOwnerId());

        if (owner == null) {
            return 0;
        }

        if (pet.getOwnerId() != adoptionModel.getOwnerId()) {
            return 0;
        }

        if (owner.getDocument() == null || owner.getDocument().isEmpty()) {
            return 0;
        }

        pet.setForAdoption(true);
        petDao.updateInformation(pet.getId(), pet);

        adoptionModel.setAdopted(false);
        adoptionModel.setReceiverId(0);
        adoptionModel.setTransferStatus(TransferStatus.NONE);
        adoptionModel.setPublicationDate(new Date(System.currentTimeMillis()));

        return adoptionDao.add(adoptionModel);
    }

    @Override
    public void delete(int id) {

        if (isIdInvalid(id)) {
            return;
        }

        AdoptionModel adoptionModel = adoptionDao.readyById(id);

        if (adoptionModel == null) {
            return;
        }

        if (adoptionModel.getTransferStatus() != TransferStatus.ACCEPTED) {
            PetModel pet = petDao.readyById(adoptionModel.getPetId());

            if (pet != null && pet.getOwnerId() == adoptionModel.getOwnerId()) {
                pet.setForAdoption(false);
                petDao.updateInformation(pet.getId(), pet);
            }
        }

        adoptionDao.remove(id);
    }

    @Override
    public AdoptionModel findById(int id) {
        if (isIdInvalid(id)) {
            return null;
        }
        return adoptionDao.readyById(id);
    }

    @Override
    public List<AdoptionModel> findAll() {
        return adoptionDao.readAll();
    }

    @Override
    public boolean update(int id, AdoptionModel adoptionModel) {
        if (isIdInvalid(id) || adoptionModel == null) {
            return false;
        }

        AdoptionModel dataToUpdate = adoptionDao.readyById(id);

        if (dataToUpdate == null) {
            return false;
        }

        if (adoptionModel.getDescription() == null || adoptionModel.getDescription().isEmpty()) {
            return false;
        }

        dataToUpdate.setDescription(adoptionModel.getDescription());
        dataToUpdate.setContact(adoptionModel.getContact());

        adoptionDao.updateInformation(id, dataToUpdate);

        return true;
    }

    boolean isIdInvalid(int id) {
        return id < 0;
    }
}