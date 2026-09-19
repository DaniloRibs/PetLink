package br.fai.faitec.petlink2026.domain.animal;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public abstract class AnimalModel {

    private int id;
    private String name;
    private Specie species;
    private String breed;
    private Date birthDate;
    private int ownerId;
    private String gender;
    private List<VaccineModel> vaccines = new ArrayList<>();


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Specie getSpecies() {
        return species;
    }

    public void setSpecies(Specie species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public List<VaccineModel> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<VaccineModel> vaccines) {
        this.vaccines = vaccines;
    }
}
