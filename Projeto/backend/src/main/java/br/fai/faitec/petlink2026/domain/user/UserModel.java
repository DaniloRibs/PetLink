package br.fai.faitec.petlink2026.domain.user;

import br.fai.faitec.petlink2026.domain.animal.AnimalModel;
import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.animal.PetModel;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

    private int id;
    private String email;
    private String password;
    private String fullname;
    private String phone;
    private String document;
    private AccountType accountType;
    private List<PetModel> pets = new ArrayList<>();
    private List<FarmAnimalModel> farmAnimalModels = new ArrayList<>();

    public List<FarmAnimalModel> getFarmAnimalModels() {
        return farmAnimalModels;
    }

    public void setFarmAnimalModels(List<FarmAnimalModel> farmAnimalModels) {
        this.farmAnimalModels = farmAnimalModels;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getDocument() {
        return document;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<PetModel> getPets() {
        return pets;
    }

    public void setPets(List<PetModel> pets) {
        this.pets = pets;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
