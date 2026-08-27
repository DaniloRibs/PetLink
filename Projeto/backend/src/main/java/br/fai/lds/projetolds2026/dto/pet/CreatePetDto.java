package br.fai.lds.projetolds2026.dto.pet;

import br.fai.lds.projetolds2026.domain.pet.Especie;
import br.fai.lds.projetolds2026.domain.pet.PetModel;

import java.sql.Timestamp;

public class CreatePetDto {

    private String name;
    private String race;
    private Timestamp anoNasc;
    private Especie especie;
    private int idOwner;


    public PetModel toPetModel() {
        final PetModel petModel = new PetModel();
        petModel.setName(name);
        petModel.setRace(race);
        petModel.setAnoNasc(anoNasc);
        petModel.setIdOwner(idOwner);
        petModel.setEspecie(especie);

        return petModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Timestamp getAnoNasc() {
        return anoNasc;
    }

    public void setAnoNasc(Timestamp anoNasc) {
        this.anoNasc = anoNasc;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }
}
