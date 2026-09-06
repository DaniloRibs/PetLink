package br.fai.lds.projetolds2026.dto.pet;

import br.fai.lds.projetolds2026.domain.pet.Especie;
import br.fai.lds.projetolds2026.domain.pet.PetModel;

import java.sql.Timestamp;

public class UpdatePetDto {

    private int id;
    private String name;
    private Especie especie;
    private String breed;
    private Timestamp anoNasc;


    public PetModel toPetModel() {
        final PetModel petModel = new PetModel();
        petModel.setId(id);
        petModel.setName(name);
        petModel.setBreed(breed);
        petModel.setAnoNasc(anoNasc);
        petModel.setEspecie(especie);

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

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
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
