package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.animal;

import br.fai.faitec.petlink2026.domain.animal.Specie;
import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.PetDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PetFakeDaoAdapter implements PetDao {

    private static final List<PetModel> entities = new ArrayList<>();
    private static int ID = 0;

    public PetFakeDaoAdapter() {

        PetModel entity1 = new PetModel();
        entity1.setId(getNextId());
        entity1.setName("Pandora");
        entity1.setSpecies(Specie.DOG);
        entity1.setOwnerId(1);
        entity1.setBreed("Lulu");
        entity1.setBirthDate(new Date(2026, 10, 12));

        PetModel entity2 = new PetModel();
        entity2.setId(getNextId());
        entity2.setName("Lucy");
        entity2.setSpecies(Specie.CAT);
        entity2.setOwnerId(2);
        entity2.setBreed("ViraLata");
        entity2.setBirthDate(new Date(2026, 10, 12));

        PetModel entity3 = new PetModel();
        entity3.setId(getNextId());
        entity3.setName("Tiririca");
        entity3.setSpecies(Specie.BIRD);
        entity3.setOwnerId(4);
        entity3.setBreed("Pardal");
        entity3.setBirthDate(new Date(2016, 03, 17));


        PetModel entity4 = new PetModel();
        entity4.setId(getNextId());
        entity4.setName("Thor");
        entity4.setSpecies(Specie.OTHER);
        entity4.setOwnerId(2);
        entity4.setBreed("Peixe beta");
        entity4.setBirthDate(new Date(2022, 11, 22));

        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);

    }

    private int getNextId() {
        ID += 1;
        return ID;
    }

    @Override
    public int add(PetModel entity) {

        entity.setId(getNextId());
        entities.add(entity);

        return entity.getId();
    }

    @Override
    public void remove(int id) {

        int itemIndex = -1;

        for (int i = 0; i < entities.size(); i++) {

            PetModel entity = entities.get(i);

            if (entity.getId() == id) {
                itemIndex = i;
                break;
            }
        }

        if (itemIndex == -1) {
            return;
        }

        PetModel removedData = entities.remove(itemIndex);

        System.out.println(
                "A entidade " + removedData.getName() + " foi removida com sucesso."
        );
    }

    @Override
    public PetModel readyById(int id) {

        for (PetModel entity : entities) {

            if (entity.getId() == id) {
                return entity;
            }
        }

        return null;
    }

    @Override
    public List<PetModel> readAll() {
        return entities;
    }

    @Override
    public void updateInformation(int id, PetModel entity) {
        PetModel data = readyById(id);

        if (data == null) {
            return;
        }

        data.setName(entity.getName());
        data.setBreed(entity.getBreed());
        data.setSpecies(entity.getSpecies());
        data.setBirthDate(entity.getBirthDate());
        data.setOwnerId(entity.getOwnerId());
    }

}