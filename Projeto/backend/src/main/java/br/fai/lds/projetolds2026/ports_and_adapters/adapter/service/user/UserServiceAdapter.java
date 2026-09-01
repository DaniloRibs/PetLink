package br.fai.lds.projetolds2026.ports_and_adapters.adapter.service.user;

import br.fai.lds.projetolds2026.domain.pet.PetModel;
import br.fai.lds.projetolds2026.domain.user.UserModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.pet.PetDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceAdapter implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PetDao petDao;

    @Override
    public int create(UserModel userModel) {

        if (userModel == null) {
            return 0;
        }

        if (isPasswordInvalid(userModel.getPassword())) {
            return 0;
        }

        if (userModel.getEmail().isEmpty()) {
            return 0;
        }

        if (userModel.getFullname().isEmpty()) {
            return 0;
        }

        if (!userModel.getEmail().contains("@")) {
            return 0;
        }

        return userDao.add(userModel);
    }

    @Override
    public void delete(int id) {
        if (isIdInvalid(id)) {
            return;
        }
        userDao.remove(id);

    }

    @Override
    public boolean update(int id, UserModel userModel) {
        UserModel dataToUpdate = findById(id);
        if (dataToUpdate == null) {
            return false;
        }

        dataToUpdate.setFullname(userModel.getFullname());
        dataToUpdate.setPhone(userModel.getPhone());
        dataToUpdate.setEmail(userModel.getEmail());

        userDao.updateInformation(id, dataToUpdate);
        return true;
    }

    @Override
    public UserModel findById(int id) {


        if (isIdInvalid(id)) {
            return null;
        }


        UserModel userModel = userDao.readyById(id);
        userModel.setPets(showALlPetsByOwnerId(id));

        return userModel;
    }

    @Override
    public List<UserModel> findAll() {

        List<UserModel> userModels = userDao.readAll();


        for (UserModel userModel : userModels) {
            userModel.setPets(showALlPetsByOwnerId(userModel.getId()));

        }


        return userModels;
    }


    @Override
    public UserModel findByEmail(String email) {
        if (email.isEmpty()) {
            return null;
        }
        if (!email.contains("@")) {
            return null;
        }

        return userDao.readByEmail(email);
    }

    @Override
    public boolean updatePassword(int id, String oldPassword, String newPassword) {

        if (isIdInvalid(id)) {
            return false;
        }

        if (isPasswordInvalid(oldPassword) || isPasswordInvalid(newPassword)) {
            return false;
        }

        UserModel userModel = userDao.readyById(id);
        if (userModel == null) {
            return false;
        }

        if (!userModel.getPassword().equals(oldPassword)) {
            return false;
        }

        return userDao.updatePassword(id, newPassword);
    }

    boolean isPasswordInvalid(String password) {
        if (password.isEmpty()) {
            return true;

        }
        return password.length() < 2 ? true : false;
    }


    boolean isIdInvalid(int id) {
        return id < 0 ? true : false;
    }

    @Override
    public PetModel findPetByOwnerId(int idOwner, int idPet) {

        if (isIdInvalid(idPet) || isIdInvalid(idOwner)) {
            return null;
        }

        UserModel userModel = userDao.readyById(idOwner);

        PetModel petModel = petDao.readyById(idPet);

        if (petModel.getIdOwner() != idOwner) {
            return null;
        }


        return petModel;
    }

    @Override
    public List<PetModel> showALlPetsByOwnerId(int idOwner) {

        if (isIdInvalid(idOwner)) {
            return List.of();
        }

        UserModel userModel = userDao.readyById(idOwner);
        if (userModel == null) {
            return List.of();
        }

        List<PetModel> petsDoDono = new ArrayList<>();

        for (PetModel pet : petDao.readAll()) {
            if (pet.getIdOwner() == idOwner) {
                petsDoDono.add(pet);
            }
        }


        return petsDoDono;
    }
}
