package br.fai.faitec.petlink2026.dto.animal;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.animal.Specie;

import java.sql.Date;

public class CreateFarmAnimalDto {

    private String name;
    private String identify;
    private String breed;
    private Date birthDate;
    private Specie specie;
    private String gender;
    private double weight;
    private int ownerId;


    public FarmAnimalModel toFarmAnimalModel() {
        final FarmAnimalModel farmAnimalModel = new FarmAnimalModel();
        if (name == null  || name.isEmpty() ){
            farmAnimalModel.setName("");
        }
        else{
            farmAnimalModel.setName(name);
        }
        farmAnimalModel.setIdentify(identify);
        farmAnimalModel.setBreed(breed);
        farmAnimalModel.setBirthDate(birthDate);
        farmAnimalModel.setOwnerId(ownerId);
        farmAnimalModel.setSpecies(specie);
        farmAnimalModel.setGender(gender);
        farmAnimalModel.setWeight(weight);
        farmAnimalModel.setForSell(false);

        return farmAnimalModel;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public Specie getSpecies() {
        return specie;
    }

    public void setSpecies(Specie specie) {
        this.specie = specie;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

}
