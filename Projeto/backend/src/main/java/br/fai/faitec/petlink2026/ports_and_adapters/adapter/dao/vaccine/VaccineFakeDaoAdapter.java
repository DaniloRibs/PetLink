package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.vaccine;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VaccineFakeDaoAdapter implements VaccineDao {

    private static final List<VaccineModel> entities = new ArrayList<>();
    private static int ID = 0;

    public VaccineFakeDaoAdapter() {
        VaccineModel vaccineModel1 = new VaccineModel();
        vaccineModel1.setName("raiva");
        vaccineModel1.setId(getNextId());
        vaccineModel1.setDescription("previne a raiva dos bicho tudo");
        vaccineModel1.setIdPet(3);
        vaccineModel1.setBatch("921");
        vaccineModel1.setApplicationDate(Timestamp.valueOf("2019-12-11 00:00:00"));
        vaccineModel1.setExpirationDate(Timestamp.valueOf("2025-12-01 00:00:00"));


        VaccineModel vaccineModel2 = new VaccineModel();
        vaccineModel2.setName("raiva");
        vaccineModel2.setId(getNextId());
        vaccineModel2.setDescription("previne a raiva dos bicho tudo");
        vaccineModel2.setIdPet(3);
        vaccineModel2.setBatch("1080");
        vaccineModel2.setApplicationDate(Timestamp.valueOf("2025-12-05 00:00:00"));
        vaccineModel2.setExpirationDate(Timestamp.valueOf("2031-11-29 00:00:00"));

        entities.add(vaccineModel1);
        entities.add(vaccineModel2);

    }

    private int getNextId() {
        ID += 1;
        return ID;
    }

    @Override
    public int add(VaccineModel entity) {
        final int id = getNextId();
        entity.setId(id);
        entities.add(entity);
        return id;
    }

    @Override
    public void remove(int id) {
        int itemIndex = -1;

        for (int i = 0; i < entities.size(); i++) {
            final VaccineModel entity = entities.get(i);
            if (entity.getId() == id) {
                itemIndex = i;
                break;
            }

        }

        if (itemIndex == -1) {
            return;
        }

        VaccineModel removedData = entities.remove(itemIndex);
        System.out.println("A entidade " + removedData.getName() + "foi removida com sucesso.");

    }

    @Override
    public VaccineModel readyById(int id) {
        for (VaccineModel entity : entities) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<VaccineModel> readAll() {
        return entities;
    }

    @Override
    public void updateInformation(int id, VaccineModel vaccineModel) {
        for (VaccineModel data : entities) {
            if (data.getId() == id) {
                data.setBatch(vaccineModel.getBatch());
                data.setName(vaccineModel.getName());
                data.setDescription(vaccineModel.getDescription());
                data.setApplicationDate(vaccineModel.getApplicationDate());
                data.setExpirationDate(vaccineModel.getExpirationDate());

                break;
            }
        }

    }
}
