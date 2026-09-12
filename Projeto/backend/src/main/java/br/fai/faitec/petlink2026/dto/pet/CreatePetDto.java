package br.fai.faitec.petlink2026.dto.pet;

import br.fai.faitec.petlink2026.domain.pet.Species;
import br.fai.faitec.petlink2026.domain.pet.PetModel;

import java.sql.Timestamp;

public class CreatePetDto {

    private String name;
    private String breed;
    private Timestamp anoNasc;
    private Species species;
    private int idOwner;


    public PetModel toPetModel() {
        final PetModel petModel = new PetModel();
        petModel.setName(name);
        petModel.setBreed(breed);
        petModel.setBirthDate(anoNasc);
        petModel.setIdOwner(idOwner);
        petModel.setEspecie(species);

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

    public Timestamp getAnoNasc() {
        return anoNasc;
    }

    public void setAnoNasc(Timestamp anoNasc) {
        this.anoNasc = anoNasc;
    }

    public Species getEspecie() {
        return species;
    }

    public void setEspecie(Species species) {
        this.species = species;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }
}
