package br.fai.faitec.petlink2026.domain.user;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.animal.PetModel;

import java.util.ArrayList;
import java.util.List;

public abstract class UserModel {

    private int id;
    private String email;
    private String password;
    private String fullname;
    private String phone;
    private List<PetModel> pets = new ArrayList<>();
    private List<FarmAnimalModel> farmAnimalModels = new ArrayList<>();

    public abstract AccountType getAccountType();

    public abstract String getDocument();

    public abstract void setDocument(String document);

    public List<FarmAnimalModel> getFarmAnimalModels() {
        return farmAnimalModels;
    }

    public void setFarmAnimalModels(List<FarmAnimalModel> farmAnimalModels) {
        this.farmAnimalModels = farmAnimalModels;
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
