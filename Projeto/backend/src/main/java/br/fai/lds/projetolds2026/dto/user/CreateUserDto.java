package br.fai.lds.projetolds2026.dto.user;

import br.fai.lds.projetolds2026.domain.user.UserModel;


public class CreateUserDto {

    private String email;
    private String password;
    private String fullname;
    private String phone;

    public UserModel toUserModel() {
        final UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setFullname(fullname);
        userModel.setPhone(phone);
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
