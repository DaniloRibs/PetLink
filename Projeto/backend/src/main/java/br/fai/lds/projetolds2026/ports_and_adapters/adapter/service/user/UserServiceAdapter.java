package br.fai.lds.projetolds2026.ports_and_adapters.adapter.service.user;

import br.fai.lds.projetolds2026.domain.pet.PetModel;
import br.fai.lds.projetolds2026.domain.user.AccountType;
import br.fai.lds.projetolds2026.domain.user.UserModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.pet.PetService;
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
    private PetService petService;

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
        if (userModel.getPhone().isEmpty()) {
            return 0;
        }

        if (!userModel.getEmail().contains("@")) {
            return 0;
        }
        if (userModel.getAccountType() == AccountType.ENTERPRISE && userModel.getDocument().isEmpty()) {
            return 0;
        }
        if (!userModel.getDocument().isEmpty()) {
            if (isDocumentInvalid(userModel.getDocument(), userModel.getAccountType())) {
                return 0;
            }
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
        dataToUpdate.setDocument(userModel.getDocument());

        userDao.updateInformation(id, dataToUpdate);
        return true;
    }

    @Override
    public UserModel findById(int id) {


        if (isIdInvalid(id)) {
            return null;
        }


        UserModel userModel = userDao.readyById(id);
        userModel.setPets(showAllPetsByOwnerId(id));

        return userModel;
    }

    @Override
    public List<UserModel> findAll() {

        List<UserModel> userModels = userDao.readAll();


        for (UserModel userModel : userModels) {
            userModel.setPets(showAllPetsByOwnerId(userModel.getId()));

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

        PetModel petModel = petService.findById(idPet);

        if (petModel.getIdOwner() != idOwner) {
            return null;
        }


        return petModel;
    }

    @Override
    public List<PetModel> showAllPetsByOwnerId(int idOwner) {

        if (isIdInvalid(idOwner)) {
            return List.of();
        }

        UserModel userModel = userDao.readyById(idOwner);
        if (userModel == null) {
            return List.of();
        }

        List<PetModel> petsDoDono = new ArrayList<>();

        for (PetModel pet : petService.findAll()) {
            if (pet.getIdOwner() == idOwner) {
                petsDoDono.add(pet);
            }
        }


        return petsDoDono;
    }


    private boolean isDocumentInvalid(String document, AccountType accountType) {

        document = document.replaceAll("\\D", "");

        if (document.length() == 11 && accountType == AccountType.PERSON) {
            return !isCpfValid(document);
        }

        if (document.length() == 14 && accountType == AccountType.ENTERPRISE) {
            return !isCnpjValid(document);
        }

        return true;
    }

    private boolean isCpfValid(String cpf) {

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;

        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }

        int digit = 11 - (sum % 11);

        if (digit >= 10) {
            digit = 0;
        }

        if (digit != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        sum = 0;

        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }

        digit = 11 - (sum % 11);

        if (digit >= 10) {
            digit = 0;
        }

        return digit == Character.getNumericValue(cpf.charAt(10));
    }

    private boolean isCnpjValid(String cnpj) {

        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;

        for (int i = 0; i < 12; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weights1[i];
        }

        int digit = 11 - (sum % 11);

        if (digit >= 10) {
            digit = 0;
        }

        if (digit != Character.getNumericValue(cnpj.charAt(12))) {
            return false;
        }

        sum = 0;

        for (int i = 0; i < 13; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weights2[i];
        }

        digit = 11 - (sum % 11);

        if (digit >= 10) {
            digit = 0;
        }

        return digit == Character.getNumericValue(cnpj.charAt(13));
    }


}
