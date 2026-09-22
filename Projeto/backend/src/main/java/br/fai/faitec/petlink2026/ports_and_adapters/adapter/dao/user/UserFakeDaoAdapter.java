package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user;

import br.fai.faitec.petlink2026.domain.user.EnterpriseModel;
import br.fai.faitec.petlink2026.domain.user.PersonModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;

import java.util.ArrayList;
import java.util.List;

public class UserFakeDaoAdapter implements UserDao {

    private static final List<UserModel> entities = new ArrayList<>();
    private static int ID = 0;

    public UserFakeDaoAdapter() {

        PersonModel entity1 = new PersonModel();
        entity1.setId(getNextId());
        entity1.setEmail("Dan@gmail.com");
        entity1.setPhone("912412412");
        entity1.setFullname("dadan");
        entity1.setPassword("123456");

        PersonModel entity2 = new PersonModel();
        entity2.setId(getNextId());
        entity2.setEmail("rod@gmail.com");
        entity2.setPhone("88567221");
        entity2.setFullname("rodhero");
        entity2.setPassword("654321");

        PersonModel entity3 = new PersonModel();
        entity3.setId(getNextId());
        entity3.setEmail("bolin@gmail.com");
        entity3.setPhone("2358511123");
        entity3.setCpf("12345678910");
        entity3.setFullname("bolo");
        entity3.setPassword("456789");

        EnterpriseModel entity4 = new EnterpriseModel();
        entity4.setId(getNextId());
        entity4.setEmail("braianEnterprise@gmail.com");
        entity4.setCnpj("123981491");
        entity4.setPhone("912761412");
        entity4.setFullname("braian");
        entity4.setPassword("12345678910123");

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
    public UserModel readyById(int id) {
        for (UserModel entity : entities) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<UserModel> readAll() {
        return entities;
    }

    @Override
    public void remove(int id) {
        int itemIndex = -1;

        for (int i = 0; i < entities.size(); i++) {
            final UserModel entity = entities.get(i);
            if (entity.getId() == id) {
                itemIndex = i;
                break;
            }

        }

        if (itemIndex == -1) {
            return;
        }

        UserModel removedData = entities.remove(itemIndex);
        System.out.println("A entidade " + removedData.getFullname() + "foi removida com sucesso.");
    }

    @Override
    public UserModel readByEmail(String email) {
        for (UserModel entity : entities) {
            if (entity.getEmail().equalsIgnoreCase(email)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public boolean updatePassword(int id, String password) {
        boolean response = false;

        for (UserModel entity : entities) {
            if (entity.getId() == id) {
                entity.setPassword(password);
                response = true;
                break;
            }
        }
        return response;
    }


}
