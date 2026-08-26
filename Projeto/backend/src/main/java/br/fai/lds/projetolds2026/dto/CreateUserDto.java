package br.fai.lds.projetolds2026.dto;

import br.fai.lds.projetolds2026.domain.UserModel;

public class CreateUserDto {

    private String email;
    private String password;
    private String fullname;

    public UserModel toUserModel() {
        final UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setFullname(fullname);

        return userModel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


}
