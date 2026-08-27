package br.fai.lds.projetolds2026.ports_and_adapters.adapter.service.pet;

import br.fai.lds.projetolds2026.domain.pet.PetModel;
import br.fai.lds.projetolds2026.domain.user.UserModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.pet.PetDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetServiceAdapter implements PetService {

    @Autowired
    private PetDao petDao;
    @Autowired
    private UserDao userDao;

    @Override
    public int create(PetModel petModel) {

        if (petModel == null) {
            return 0;
        }

        if (petModel.getName() == null || petModel.getName().isEmpty()) {
            return 0;
        }

        if (petModel.getIdOwner() <= 0) {
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
        return petDao.readyById(id);
    }

    @Override
    public List<PetModel> findALl() {
        return petDao.readAll();
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
        dataToUpdate.setAnoNasc(petModel.getAnoNasc());
        dataToUpdate.setRace(petModel.getRace());

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
        petDao.updateOwner(id, newOwner);

        newOwnerUser.getPets().add(pet);

        return true;
    }

    @Override
    public boolean setOwner(int id, int owner) {
        PetModel pet = petDao.readyById(id);

        if (pet == null) {
            return false;
        }

        UserModel newUser = userDao.readyById(owner);

        if (newUser == null) {
            return false;
        }

        int oldOwnerId = pet.getIdOwner();
        if (oldOwnerId > 0) {
            UserModel oldUser = userDao.readyById(oldOwnerId);
            if (oldUser != null) {
                oldUser.getPets().remove(pet);
            }
        }

        petDao.setOwner(id, owner);

        newUser.getPets().add(pet);

        return true;
    }


    boolean isIdInvalid(int id) {
        return id < 0 ? true : false;
    }
}
