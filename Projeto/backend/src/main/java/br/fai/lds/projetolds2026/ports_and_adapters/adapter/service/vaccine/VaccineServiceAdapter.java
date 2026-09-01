package br.fai.lds.projetolds2026.ports_and_adapters.adapter.service.vaccine;

import br.fai.lds.projetolds2026.domain.pet.PetModel;
import br.fai.lds.projetolds2026.domain.vaccine.VaccineModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.pet.PetDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.vaccine.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VaccineServiceAdapter implements VaccineService {

    @Autowired
    private VaccineModel vaccineModel;
    @Autowired
    private VaccineDao vaccineDao;
    @Autowired
    private PetDao petDao;

    @Override
    public int create(VaccineService entity) {


        if (vaccineDao == null) {
            return 0;
        }

        if (vaccineModel.getName().isEmpty()) {
            return 0;
        }


        return 0;
    }

    @Override
    public void delete(int id) {

        if (isIdInvalid(id)) {
            return;
        }
        //VaccineDao.remove(id);

    }

    boolean isIdInvalid(int id) {
        return id < 0 ? true : false;
    }


    @Override
    public VaccineService findById(int id) {
        return null;
    }

    @Override
    public List<VaccineService> findALl() {
        return List.of();
    }

    @Override
    public boolean update(int id, VaccineService entity) {
        return false;
    }
}
