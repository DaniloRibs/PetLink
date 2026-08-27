package br.fai.lds.projetolds2026.dto.user;

import br.fai.lds.projetolds2026.domain.user.UserModel;

public class UpdateUserNameDto {

    private int id;
    private String fullname;

    public UserModel toUserModel() {
        final UserModel entity = new UserModel();
        entity.setId(id);
        entity.setFullname(fullname);
        return entity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


}
