package br.fai.faitec.petlink2026.dto.user;

import br.fai.faitec.petlink2026.domain.user.UserModel;

public class UpdateUserDto {

    private int id;
    private String fullname;
    private String email;
    private String phone;
    private String document;


    public UserModel toUserModel() {
        final UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setFullname(fullname);
        userModel.setEmail(email);
        userModel.setPhone(phone);
        userModel.setDocument(document);
        return userModel;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
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
