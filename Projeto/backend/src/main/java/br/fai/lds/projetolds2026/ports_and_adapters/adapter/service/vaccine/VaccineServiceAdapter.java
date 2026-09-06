package br.fai.lds.projetolds2026.ports_and_adapters.adapter.service.vaccine;

import br.fai.lds.projetolds2026.domain.user.UserModel;
import br.fai.lds.projetolds2026.domain.vaccine.VaccineModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.pet.PetDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.vaccine.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VaccineServiceAdapter implements VaccineService {

    @Autowired
    private VaccineDao vaccineDao;


    @Override
    public int create(VaccineModel vaccineModel) {

        if (vaccineModel == null) {
            return 0;
        }

        if (vaccineModel.getName().isEmpty()) {
            return 0;
        }
        if (vaccineModel.getExpirationDate() == null) {
            return 0;
        }
        if (vaccineModel.getApplicationDate() == null) {
            return 0;
        }
        if (vaccineModel.getLote().isEmpty()) {
            return 0;
        }
        if (isIdInvalid(vaccineModel.getIdPet())) {
            return 0;
        }
        if (isIdInvalid(vaccineModel.getId())) {
            return 0;
        }


        return vaccineDao.add(vaccineModel);
    }

    @Override
    public void delete(int id) {
        if (isIdInvalid(id)) {
            return;
        }
        vaccineDao.remove(id);
    }

    boolean isIdInvalid(int id) {
        return id < 0 ? true : false;
    }

    @Override
    public VaccineModel findById(int id) {
        if (isIdInvalid(id)) {
            return null;
        }

        VaccineModel vaccineModel = vaccineDao.readyById(id);

        return vaccineModel;
    }

    @Override
    public List<VaccineModel> findAll() {
        List<VaccineModel> vaccines = vaccineDao.readAll();
        return vaccines;
    }

    @Override
    public boolean update(int id, VaccineModel vaccineModel) {

        VaccineModel dataToUpdate = findById(id);
        if (dataToUpdate == null) {
            return false;
        }
        dataToUpdate.setLote(vaccineModel.getLote());
        dataToUpdate.setName(vaccineModel.getName());
        dataToUpdate.setDescription(vaccineModel.getDescription());
        dataToUpdate.setApplicationDate(vaccineModel.getApplicationDate());
        dataToUpdate.setExpirationDate(vaccineModel.getExpirationDate());

        vaccineDao.updateInformation(id, dataToUpdate);
        return true;
    }
}
