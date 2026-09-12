package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.pet;

import br.fai.faitec.petlink2026.domain.pet.PetModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.pet.PetDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetServiceAdapter implements PetService {

    @Autowired
    private PetDao petDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private VaccineDao vaccineDao;

    @Override
    public int create(PetModel petModel) {

        if (petModel == null) {
            return 0;
        }

        if (petModel.getName() == null || petModel.getName().isEmpty()) {
            return 0;
        }

        if (isIdInvalid(petModel.getId())) {
            return 0;
        }

        UserModel owner = userDao.readyById(petModel.getIdOwner());

        if (owner == null) {
            return 0;
        }

        int id = petDao.add(petModel);

        if (id == 0) {
            return 0;
        }

        owner.getPets().add(petModel);

        return id;
    }

    @Override
    public void delete(int id) {
        PetModel pet = petDao.readyById(id);

        if (pet == null) {
            return;
        }

        UserModel owner = userDao.readyById(pet.getIdOwner());

        if (owner != null) {
            owner.getPets().remove(pet);
        }

        petDao.remove(id);

    }

    @Override
    public PetModel findById(int id) {
        if (isIdInvalid(id)) {
            return null;
        }
        PetModel petModel = petDao.readyById(id);

        petModel.setVaccines(showAllVaccineByPetId(id));

        return petModel;
    }

    @Override
    public List<PetModel> findAll() {
        List<PetModel> pets = petDao.readAll();


        for (PetModel petModel : pets) {
            petModel.setVaccines(showAllVaccineByPetId(petModel.getId()));

        }


        return pets;
    }

    @Override
    public boolean update(int id, PetModel petModel) {
        if (isIdInvalid(id) || petModel == null) {
            return false;
        }

        PetModel dataToUpdate = findById(id);

        if (dataToUpdate == null) {
            return false;
        }

        if (petModel.getName() == null || petModel.getName().isEmpty()) {
            return false;
        }

        dataToUpdate.setName(petModel.getName());
        dataToUpdate.setEspecie(petModel.getEspecie());
        dataToUpdate.setBirthDate(petModel.getBirthDate());
        dataToUpdate.setBreed(petModel.getBreed());

        petDao.updateInformation(id, dataToUpdate);

        return true;
    }

    @Override
    public boolean updateOwner(int id, int oldOwner, int newOwner) {
        if (isIdInvalid(id) || oldOwner <= 0 || newOwner <= 0) {
            return false;
        }

        PetModel pet = petDao.readyById(id);

        if (pet == null || pet.getIdOwner() != oldOwner) {
            return false;
        }

        UserModel newOwnerUser = userDao.readyById(newOwner);
        UserModel oldOwnerUser = userDao.readyById(oldOwner);

        if (newOwnerUser == null || oldOwnerUser == null) {
            return false;
        }

        oldOwnerUser.getPets().remove(pet);

        pet.setIdOwner(newOwner);
        petDao.updateInformation(id, pet);

        return true;
    }


    boolean isIdInvalid(int id) {
        return id < 0 ? true : false;
    }

    @Override
    public VaccineModel findVaccineByPetId(int idPet, int idVaccine) {

        if (isIdInvalid(idPet) || isIdInvalid(idVaccine)) {
            return null;
        }

        VaccineModel vaccineModel = vaccineDao.readyById(idVaccine);

        if (vaccineModel == null) {
            return null;
        }

        if (vaccineModel.getIdPet() != idPet) {
            return null;
        }

        return vaccineModel;
    }

    @Override
    public List<VaccineModel> showAllVaccineByPetId(int idPet) {

        if (isIdInvalid(idPet)) {
            return List.of();
        }

        List<VaccineModel> vaccinesPet = new ArrayList<>();

        for (VaccineModel vaccine : vaccineDao.readAll()) {

            if (vaccine.getIdPet() == idPet) {
                vaccinesPet.add(vaccine);
            }
        }

        return vaccinesPet;
    }
}
