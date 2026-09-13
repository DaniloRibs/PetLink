package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.adoption;

import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;
import br.fai.faitec.petlink2026.domain.pet.PetModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.adoption.AdoptionDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.pet.PetDao;
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

        adoptionModel.setAdopted(false);
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

    @Override
    public boolean markAsAdopted(int id) {
        if (isIdInvalid(id)) {
            return false;
        }

        AdoptionModel adoptionModel = adoptionDao.readyById(id);

        if (adoptionModel == null) {
            return false;
        }

        adoptionModel.setAdopted(true);
        adoptionDao.updateInformation(id, adoptionModel);

        return true;
    }

    boolean isIdInvalid(int id) {
        return id < 0;
    }
}