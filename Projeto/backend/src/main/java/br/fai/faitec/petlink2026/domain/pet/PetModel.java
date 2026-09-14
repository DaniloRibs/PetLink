package br.fai.faitec.petlink2026.domain.pet;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class PetModel {

    private int id;
    private String name;
    private Specie species;
    private String breed;
    private Date birthDate;
    private int ownerId;
    private boolean forAdoption;
    private List<VaccineModel> vaccines = new ArrayList<>();


    public boolean isForAdoption() {
        return forAdoption;
    }

    public void setForAdoption(boolean forAdoption) {
        this.forAdoption = forAdoption;
    }

    public List<VaccineModel> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<VaccineModel> vaccines) {
        this.vaccines = vaccines;
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

    public Specie getSpecies() {
        return species;
    }

    public void setSpecies(Specie species) {
        this.species = species;
    }
}
