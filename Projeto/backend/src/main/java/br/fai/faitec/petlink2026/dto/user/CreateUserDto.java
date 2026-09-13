package br.fai.faitec.petlink2026.dto.user;

import br.fai.faitec.petlink2026.domain.user.AccountType;
import br.fai.faitec.petlink2026.domain.user.UserModel;


public class CreateUserDto {

    private String email;
    private String password;
    private String fullname;
    private String phone;
    private String document;
    private AccountType accountType;

    public UserModel toUserModel() {
        final UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setFullname(fullname);
        userModel.setPhone(phone);
        userModel.setDocument(document);
        userModel.setAccountType(accountType);

        return userModel;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
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
