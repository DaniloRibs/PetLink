package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.user;

import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;
import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.domain.announcement.AnnouncementModel;
import br.fai.faitec.petlink2026.domain.user.AccountType;
import br.fai.faitec.petlink2026.domain.user.EnterpriseModel;
import br.fai.faitec.petlink2026.domain.user.PersonModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.announcement.AnnouncementDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.EnterpriseDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.PersonDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.adoption.AdoptionService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.FarmAnimalService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.PetService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.vaccine.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceAdapter implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private PetService petService;
    @Autowired
    private FarmAnimalService farmAnimalService;
    @Autowired
    private VaccineService vaccineService;
    @Autowired
    private AdoptionService adoptionService;
    @Autowired
    private AnnouncementDao announcementDao;

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

        if (isContactInvalid(userModel.getPhone(), userModel.getDocument(), userModel.getAccountType())) {
            return 0;
        }

        if (userModel instanceof PersonModel personModel) {
            return personDao.add(personModel);
        }

        if (userModel instanceof EnterpriseModel enterpriseModel) {
            return enterpriseDao.add(enterpriseModel);
        }

        return 0;
    }

    @Override
    public void delete(int id) {
        if (isIdInvalid(id)) {
            return;
        }

        UserModel userModel = userDao.readyById(id);
        if (userModel == null) {
            return;
        }

        for (PetModel pet : showAllPetsByOwnerId(id)) {
            for (VaccineModel vaccine : petService.showAllVaccineByAnimalId(pet.getId())) {
                vaccineService.delete(vaccine.getId());
            }
            petService.delete(pet.getId());
        }

        for (AdoptionModel adoption : adoptionService.findAll()) {
            if (adoption.getOwnerId() == id) {
                adoptionService.delete(adoption.getId());
            }
        }

        for (AnnouncementModel announcement : announcementDao.readAll()) {
            if (announcement.getIdCreator() == id) {
                announcementDao.remove(announcement.getId());
            }
        }

        userDao.remove(id);

    }

    @Override
    public boolean update(int id, UserModel userModel) {
        UserModel dataToUpdate = findById(id);
        if (dataToUpdate == null) {
            return false;
        }

        if (!isBlank(userModel.getPhone()) && isPhoneInvalid(userModel.getPhone())) {
            return false;
        }

        if (isBlank(dataToUpdate.getDocument()) && !isBlank(userModel.getDocument())) {
            if (isDocumentInvalid(userModel.getDocument(), dataToUpdate.getAccountType())) {
                return false;
            }
            dataToUpdate.setDocument(userModel.getDocument());
        }

        dataToUpdate.setFullname(userModel.getFullname());
        dataToUpdate.setPhone(userModel.getPhone());
        dataToUpdate.setEmail(userModel.getEmail());

        if (dataToUpdate instanceof PersonModel personModel) {
            personDao.updateInformation(id, personModel);
            return true;
        }

        if (dataToUpdate instanceof EnterpriseModel enterpriseModel) {
            enterpriseDao.updateInformation(id, enterpriseModel);
            return true;
        }

        return false;
    }

    @Override
    public UserModel findById(int id) {


        if (isIdInvalid(id)) {
            return null;
        }


        UserModel userModel = userDao.readyById(id);

        if (userModel == null) {
            return null;
        }

        userModel.setPets(showAllPetsByOwnerId(id));
        userModel.setFarmAnimalModels(showAllAnimalByOwnerId(id));
        return userModel;
    }

    @Override
    public List<UserModel> findAll() {

        List<UserModel> userModels = userDao.readAll();


        for (UserModel userModel : userModels) {
            userModel.setPets(showAllPetsByOwnerId(userModel.getId()));
            userModel.setFarmAnimalModels(showAllAnimalByOwnerId(userModel.getId()));

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

        if (petModel == null || petModel.getOwnerId() != idOwner) {
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
            if (pet.getOwnerId() == idOwner) {
                petsDoDono.add(pet);
            }
        }


        return petsDoDono;
    }

    @Override
    public FarmAnimalModel findAnimalByOwnerId(int idOwner, int idPet) {

        if (isIdInvalid(idPet) || isIdInvalid(idOwner)) {
            return null;
        }

        UserModel userModel = userDao.readyById(idOwner);

        FarmAnimalModel farmAnimalModel = farmAnimalService.findById(idPet);

        if (farmAnimalModel == null || farmAnimalModel.getOwnerId() != idOwner) {
            return null;
        }


        return farmAnimalModel;
    }

    @Override
    public List<FarmAnimalModel> showAllAnimalByOwnerId(int idOwner) {
        if (isIdInvalid(idOwner)) {
            return List.of();
        }

        UserModel userModel = userDao.readyById(idOwner);
        if (userModel == null) {
            return List.of();
        }

        List<FarmAnimalModel> farmAnimalModels = new ArrayList<>();

        for (FarmAnimalModel FarmAnimalModel : farmAnimalService.findAll()) {
            if (FarmAnimalModel.getOwnerId() == idOwner) {
                farmAnimalModels.add(FarmAnimalModel);
            }
        }


        return farmAnimalModels;
    }


    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }

    private boolean isContactInvalid(String phone, String document, AccountType accountType) {

        if (!isBlank(phone) && isPhoneInvalid(phone)) {
            return true;
        }

        if (accountType == AccountType.ENTERPRISE && isBlank(document)) {
            return true;
        }

        if (!isBlank(document) && isDocumentInvalid(document, accountType)) {
            return true;
        }

        return false;
    }

    private boolean isPhoneInvalid(String phone) {
        final String digits = phone.replaceAll("\\D", "");
        return digits.length() != 10 && digits.length() != 11;
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
