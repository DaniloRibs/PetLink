package br.fai.lds.projetolds2026.dto.user;

import br.fai.lds.projetolds2026.domain.user.UserModel;

public class UpdateUserDto {

    private int id;
    private String fullname;
    private String email;
    private String phone;


    public UserModel toUserModel() {
        final UserModel entity = new UserModel();
        entity.setId(id);
        entity.setFullname(fullname);
        entity.setEmail(email);
        entity.setPhone(phone);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
