package br.fai.lds.projetolds2026.ports_and_adapters.adapter.dao.pet;

import br.fai.lds.projetolds2026.domain.pet.Especie;
import br.fai.lds.projetolds2026.domain.pet.PetModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.pet.PetDao;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PetFakeDaoAdapter implements PetDao {

    private static final List<PetModel> entities = new ArrayList<>();
    private static int ID = 0;

    public PetFakeDaoAdapter() {

        PetModel entity1 = new PetModel();
        entity1.setId(getNextId());
        entity1.setName("Pandora");
        entity1.setEspecie(Especie.CACHORRO);
        entity1.setIdOwner(1);
        entity1.setRace("Lulu");
        entity1.setAnoNasc(Timestamp.valueOf("2019-08-01 00:00:00"));

        PetModel entity2 = new PetModel();
        entity2.setId(getNextId());
        entity2.setName("Lucy");
        entity2.setEspecie(Especie.CACHORRO);
        entity2.setIdOwner(2);
        entity2.setRace("ViraLata");
        entity2.setAnoNasc(Timestamp.valueOf("2016-05-01 00:00:00"));

        entities.add(entity1);
        entities.add(entity2);
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
        data.setRace(entity.getRace());
        data.setEspecie(entity.getEspecie());
        data.setAnoNasc(entity.getAnoNasc());
    }

    @Override
    public boolean setOwner(int id, int owner) {

        PetModel pet = readyById(id);

        if (pet == null) {
            return false;
        }

        pet.setIdOwner(owner);

        return true;
    }

    @Override
    public boolean updateOwner(int id, int owner) {

        PetModel pet = readyById(id);

        if (pet == null) {
            return false;
        }

        pet.setIdOwner(owner);

        return true;
    }
}