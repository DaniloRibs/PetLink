package br.fai.faitec.petlink2026.domain.pet;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PetModel {

    private int id;
    private String name;
    private Species species;
    private String breed;
    private Timestamp birthDate;
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

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public Species getEspecie() {
        return species;
    }

    public void setEspecie(Species species) {
        this.species = species;
    }
}
