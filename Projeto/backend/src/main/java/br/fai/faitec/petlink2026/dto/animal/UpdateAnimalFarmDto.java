package br.fai.faitec.petlink2026.dto.animal;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;

import java.sql.Date;

public class UpdateAnimalFarmDto {

    private int id;
    private String name;
    private String breed;
    private Date birthDate;
    private double weight;
    private boolean forSeel;


    public FarmAnimalModel toFarmAnimalModel() {
        final FarmAnimalModel farmAnimalModel = new FarmAnimalModel();
        farmAnimalModel.setId(id);
        farmAnimalModel.setWeight(weight);
        farmAnimalModel.setName(name);
        farmAnimalModel.setBreed(breed);
        farmAnimalModel.setBirthDate(birthDate);
        farmAnimalModel.setForSell(forSeel);

        return farmAnimalModel;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isForSeel() {
        return forSeel;
    }

    public void setForSeel(boolean forSeel) {
        this.forSeel = forSeel;
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
