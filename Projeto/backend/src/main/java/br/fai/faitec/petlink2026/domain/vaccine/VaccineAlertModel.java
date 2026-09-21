package br.fai.faitec.petlink2026.domain.vaccine;

import br.fai.faitec.petlink2026.domain.animal.Specie;

import java.sql.Date;


public class VaccineAlertModel {

    private int animalId;
    private String animalName;
    private Specie species;
    private boolean farmAnimal;
    private int vaccineId;
    private String vaccineName;
    private Date applicationDate;
    private Date expirationDate;
    private VaccineAlertStatus status;
    private long daysOverdue;
    private long daysUntilExpiration;

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public Specie getSpecies() {
        return species;
    }

    public void setSpecies(Specie species) {
        this.species = species;
    }

    public boolean isFarmAnimal() {
        return farmAnimal;
    }

    public void setFarmAnimal(boolean farmAnimal) {
        this.farmAnimal = farmAnimal;
    }

    public int getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(int vaccineId) {
        this.vaccineId = vaccineId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public VaccineAlertStatus getStatus() {
        return status;
    }

    public void setStatus(VaccineAlertStatus status) {
        this.status = status;
    }

    public long getDaysOverdue() {
        return daysOverdue;
    }

    public void setDaysOverdue(long daysOverdue) {
        this.daysOverdue = daysOverdue;
    }

    public long getDaysUntilExpiration() {
        return daysUntilExpiration;
    }

    public void setDaysUntilExpiration(long daysUntilExpiration) {
        this.daysUntilExpiration = daysUntilExpiration;
    }
}
