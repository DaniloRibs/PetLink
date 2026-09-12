package br.fai.faitec.petlink2026.domain.pet;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class PetModel {

    private int id;
    private String name;
    private Species species;
    private String breed;
    private Date birthDate;
    private int idOwner;
    private List<VaccineModel> vaccines = new ArrayList<>();

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

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public Species getSpecie() {
        return species;
    }

    public void setSpecie(Species species) {
        this.species = species;
    }
}
