package br.fai.faitec.petlink2026.dto.pet;

import br.fai.faitec.petlink2026.domain.pet.Specie;
import br.fai.faitec.petlink2026.domain.pet.PetModel;

import java.sql.Date;

public class UpdatePetDto {

    private int id;
    private String name;
    private Specie specie;
    private String breed;
    private Date birthDate;


    public PetModel toPetModel() {
        final PetModel petModel = new PetModel();
        petModel.setId(id);
        petModel.setName(name);
        petModel.setBreed(breed);
        petModel.setBirthDate(birthDate);
        petModel.setSpecie(specie);

        return petModel;
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

    public Specie getEspecie() {
        return specie;
    }

    public void setEspecie(Specie specie) {
        this.specie = specie;
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
