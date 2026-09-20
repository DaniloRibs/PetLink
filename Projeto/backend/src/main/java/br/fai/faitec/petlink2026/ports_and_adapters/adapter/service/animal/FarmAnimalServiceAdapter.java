package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.animal;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.FarmAnimalDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.FarmAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FarmAnimalServiceAdapter implements FarmAnimalService {

    @Autowired
    private FarmAnimalDao farmAnimalDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private VaccineDao vaccineDao;

    @Override
    public VaccineModel findVaccineByAnimalId(int idPet, int idVaccine) {

        if (isIdInvalid(idPet) || isIdInvalid(idVaccine)) {
            return null;
        }

        VaccineModel vaccineModel = vaccineDao.readyById(idVaccine);

        if (vaccineModel == null) {
            return null;
        }

        if (vaccineModel.getPetId() != idPet) {
            return null;
        }

        return vaccineModel;
    }

    @Override
    public List<VaccineModel> showAllVaccineByAnimalId(int idPet) {
        if (isIdInvalid(idPet)) {
            return List.of();
        }

        List<VaccineModel> vaccinesPet = new ArrayList<>();

        for (VaccineModel vaccine : vaccineDao.readAll()) {

            if (vaccine.getPetId() == idPet) {
                vaccinesPet.add(vaccine);
            }
        }

        return vaccinesPet;
    }

    @Override
    public boolean updateOwner(int id, int oldOwner, int newOwner) {
        if (isIdInvalid(id) || oldOwner <= 0 || newOwner <= 0) {
            return false;
        }

        FarmAnimalModel farmAnimalModel = farmAnimalDao.readyById(id);

        if (farmAnimalModel == null || farmAnimalModel.getOwnerId() != oldOwner) {
            return false;
        }

        if (!farmAnimalModel.isForSell()) {
            return false;
        }

        UserModel newOwnerUser = userDao.readyById(newOwner);
        UserModel oldOwnerUser = userDao.readyById(oldOwner);

        if (newOwnerUser == null || oldOwnerUser == null) {
            return false;
        }

        oldOwnerUser.getFarmAnimalModels().remove(farmAnimalModel);

        farmAnimalModel.setOwnerId(newOwner);
        farmAnimalModel.setForSell(false);
        farmAnimalDao.updateInformation(id, farmAnimalModel);

        return true;
    }

    @Override
    public int create(FarmAnimalModel farmAnimalModel) {

        if (farmAnimalModel == null) {
            return 0;
        }

        if (farmAnimalModel.getName() == null || farmAnimalModel.getName().isEmpty()) {
            farmAnimalModel.setName("");
        }

        if (farmAnimalModel.getIdentify() == null || farmAnimalModel.getIdentify().isEmpty()) {
            return 0;
        }

        if (isIdInvalid(farmAnimalModel.getId())) {
            return 0;
        }

        UserModel owner = userDao.readyById(farmAnimalModel.getOwnerId());

        if (owner == null) {
            return 0;
        }

        int id = farmAnimalDao.add(farmAnimalModel);

        if (id == 0) {
            return 0;
        }

        owner.getFarmAnimalModels().add(farmAnimalModel);
        return id;
    }

    boolean isIdInvalid(int id) {
        return id < 0 ? true : false;
    }

    @Override
    public void delete(int id) {
        FarmAnimalModel farmAnimalModel = farmAnimalDao.readyById(id);

        if (farmAnimalModel == null) {
            return;
        }

        UserModel owner = userDao.readyById(farmAnimalModel.getOwnerId());

        if (owner != null) {
            owner.getFarmAnimalModels().remove(farmAnimalModel);
        }

        farmAnimalDao.remove(id);

    }

    @Override
    public FarmAnimalModel findById(int id) {
        if (isIdInvalid(id)) {
            return null;
        }
        FarmAnimalModel farmAnimalModel = farmAnimalDao.readyById(id);

        farmAnimalModel.setVaccines(showAllVaccineByAnimalId(id));

        return farmAnimalModel;
    }

    @Override
    public List<FarmAnimalModel> findAll() {
        List<FarmAnimalModel> farmAnimalModels = farmAnimalDao.readAll();

        for (FarmAnimalModel farmAnimalModel : farmAnimalModels) {
            farmAnimalModel.setVaccines(showAllVaccineByAnimalId(farmAnimalModel.getId()));

        }

        return farmAnimalModels;
    }

    @Override
    public boolean update(int id, FarmAnimalModel farmAnimalModel) {
        if (isIdInvalid(id) || farmAnimalModel == null) {
            return false;
        }

        FarmAnimalModel dataToUpdate = findById(id);

        if (dataToUpdate == null) {
            return false;
        }

        if (farmAnimalModel.getBreed() == null || farmAnimalModel.getBreed().isEmpty()) {
            return false;
        }

        if (farmAnimalModel.getName() == null || farmAnimalModel.getName().isEmpty()) {
            farmAnimalModel.setName("");
        }

        dataToUpdate.setName(farmAnimalModel.getName());
        dataToUpdate.setBirthDate(farmAnimalModel.getBirthDate());
        dataToUpdate.setBreed(farmAnimalModel.getBreed());
        dataToUpdate.setWeight(farmAnimalModel.getWeight());
        dataToUpdate.setForSell(farmAnimalModel.isForSell());

        farmAnimalDao.updateInformation(id, dataToUpdate);

        return true;
    }
}
