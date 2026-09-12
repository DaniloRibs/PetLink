package br.fai.faitec.petlink2026.dto.pet;

import br.fai.faitec.petlink2026.domain.pet.Species;
import br.fai.faitec.petlink2026.domain.pet.PetModel;

import java.sql.Timestamp;

public class UpdatePetDto {

    private int id;
    private String name;
    private Species species;
    private String breed;
    private Timestamp anoNasc;


    public PetModel toPetModel() {
        final PetModel petModel = new PetModel();
        petModel.setId(id);
        petModel.setName(name);
        petModel.setBreed(breed);
        petModel.setBirthDate(anoNasc);
        petModel.setEspecie(species);

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

    public Species getEspecie() {
        return species;
    }

    public void setEspecie(Species species) {
        this.species = species;
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
}
