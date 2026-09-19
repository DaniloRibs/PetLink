package br.fai.faitec.petlink2026.dto.animal;

import br.fai.faitec.petlink2026.domain.animal.PetModel;

import java.sql.Date;

public class UpdatePetDto {

    private int id;
    private String name;
    private String breed;
    private Date birthDate;
    private boolean forAdoption;


    public PetModel toPetModel() {
        final PetModel petModel = new PetModel();
        petModel.setId(id);
        petModel.setName(name);
        petModel.setBreed(breed);
        petModel.setBirthDate(birthDate);
        petModel.setForAdoption(forAdoption);

        return petModel;
    }

    public boolean isForAdoption() {
        return forAdoption;
    }

    public void setForAdoption(boolean forAdoption) {
        this.forAdoption = forAdoption;
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
}
