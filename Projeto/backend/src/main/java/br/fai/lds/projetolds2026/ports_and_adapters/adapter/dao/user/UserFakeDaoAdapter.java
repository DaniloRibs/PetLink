package br.fai.lds.projetolds2026.ports_and_adapters.adapter.dao.user;

import br.fai.lds.projetolds2026.domain.UserModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user.UserDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserFakeDaoAdapter implements UserDao {

    private static final List<UserModel> entities = new ArrayList<>();
    private static int ID = 0;

    public UserFakeDaoAdapter(){

        UserModel entity1 = new UserModel();
        entity1.setId(getNextId());
        entity1.setEmail("tiburssin@gmail.com");
        entity1.setFullname("Tiburssin Tibussius");
        entity1.setPassword("123456");

        UserModel entity2 = new UserModel();
        entity2.setId(getNextId());
        entity2.setEmail("aroldo@gmail.com");
        entity2.setFullname("Aroldo Aroldus");
        entity2.setPassword("654321");

        UserModel entity3 = new UserModel();
        entity3.setId(getNextId());
        entity3.setEmail("toninho@gmail.com");
        entity3.setFullname("Toninho Toninhus");
        entity3.setPassword("456789");

        UserModel entity4 = new UserModel();
        entity4.setId(getNextId());
        entity4.setEmail("gumercino@gmail.com");
        entity4.setFullname("Gumercino Matador");
        entity4.setPassword("987654");

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
            if (entity.getId() == id){
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

        if(itemIndex == -1) {
            return;
        }

        UserModel removedData = entities.remove(itemIndex);
        System.out.println("A entidade " + removedData.getFullname() +  "foi removida com sucesso.");
    }


    @Override
    public int add(UserModel entity) {
        final int id = getNextId();
        entity.setId(id);
        entities.add(entity);
        return id;
    }

    @Override
    public void updateInformation(int id, UserModel entity) {
        for (UserModel data : entities){
            if(data.getId() == id){
                data.setFullname(entity.getFullname());
                data.setEmail(entity.getEmail());
                break;
            }
        }
    }

    @Override
    public UserModel readByEmail(String email) {
        for (UserModel entity : entities){
            if(entity.getEmail().equalsIgnoreCase(email)){
                return entity;
            }
        }
        return null;
    }

    @Override
    public boolean updatePassword(int id, String password) {
        boolean response = false;

        for(UserModel entity : entities) {
            if(entity.getId() == id) {
                entity.setPassword(password);
                response = true;
                break;
            }
        }
        return response;
    }
}
