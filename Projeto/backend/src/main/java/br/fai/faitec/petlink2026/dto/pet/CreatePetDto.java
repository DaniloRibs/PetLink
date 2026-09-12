package br.fai.faitec.petlink2026.dto.pet;

import br.fai.faitec.petlink2026.domain.pet.Specie;
import br.fai.faitec.petlink2026.domain.pet.PetModel;

import java.sql.Date;

public class CreatePetDto {

    private String name;
    private String breed;
    private Date birthDate;
    private Specie specie;
    private int ownerId;


    public PetModel toPetModel() {
        final PetModel petModel = new PetModel();
        petModel.setName(name);
        petModel.setBreed(breed);
        petModel.setBirthDate(birthDate);
        petModel.setOwnerId(ownerId);
        petModel.setSpecie(specie);

        return petModel;
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

    public Specie getEspecie() {
        return specie;
    }

    public void setEspecie(Specie specie) {
        this.specie = specie;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
