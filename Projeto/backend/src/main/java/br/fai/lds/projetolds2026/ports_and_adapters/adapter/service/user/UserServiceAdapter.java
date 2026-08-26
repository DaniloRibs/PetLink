package br.fai.lds.projetolds2026.ports_and_adapters.adapter.service.user;

import br.fai.lds.projetolds2026.domain.UserModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceAdapter implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public int create(UserModel userModel) {

        if (userModel == null){
            return 0;
        }

        if(isPasswordInvalid(userModel.getPassword())){
        return 0;
        }

        if (userModel.getEmail().isEmpty()){
        return 0;
        }

        if (userModel.getFullname().isEmpty()){
        return 0;
        }

        if (!userModel.getEmail().contains("@")){
        return 0;
        }

        return userDao.add(userModel);
    }

    @Override
    public void delete(int id) {
        if(isIdInvalid(id)){
            return;
        }
        userDao.remove(id);

    }

    @Override
    public boolean update(int id, UserModel userModel) {
        UserModel dataToUpdate =  findById(id);
        if(dataToUpdate == null){
            return false;
        }

        dataToUpdate.setFullname(userModel.getFullname());

        userDao.updateInformation(id,dataToUpdate);
        return true;
    }

    @Override
    public UserModel findById(int id) {
        if (isIdInvalid(id)){
            // observação: o correto seria lançar uma exception
            return null;
        }
        return userDao.readyById(id);
    }

    @Override
    public List<UserModel> findALl() {
        return userDao.readAll();
    }

    @Override
    public UserModel findByEmail(String email) {
        if (email.isEmpty()){
            return null;
        }
        if (!email.contains("@")){
            return null;
        }

        return userDao.readByEmail(email);
    }

    @Override
    public boolean updatePassword(int id, String oldPassword, String newPassword) {
        // solid principles - S: responsabilidade unica
        // clean code
        if(isIdInvalid(id)){
            return false;
        }

        if(isPasswordInvalid(oldPassword) || isPasswordInvalid(newPassword)){
            return false;
        }

        UserModel userModel = userDao.readyById(id);
        if (userModel == null){
            return false;
        }

        if(!userModel.getPassword().equals(oldPassword)) {
            return false;
        }

        return userDao.updatePassword(id, newPassword);
    }


    boolean isIdInvalid(int id) {
        return id < 0 ? true : false;
    }

    boolean isPasswordInvalid(String password) {
        if (password.isEmpty()) {
            return true;

        }
        return password.length() < 2 ? true : false;
    }
}
