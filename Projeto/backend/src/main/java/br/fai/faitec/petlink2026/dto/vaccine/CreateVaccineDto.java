package br.fai.faitec.petlink2026.dto.vaccine;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;

import java.sql.Timestamp;

public class CreateVaccineDto {

    private Timestamp applicationDate;
    private Timestamp expirationDate;
    private String name;
    private String description;
    private String lote;
    private int idPet;

    public VaccineModel toVaccineModel() {
        VaccineModel vaccineModel = new VaccineModel();
        vaccineModel.setName(name);
        vaccineModel.setBatch(lote);
        vaccineModel.setDescription(description);
        vaccineModel.setIdPet(idPet);
        vaccineModel.setExpirationDate(expirationDate);
        vaccineModel.setApplicationDate(applicationDate);

        return vaccineModel;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Timestamp getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Timestamp applicationDate) {
        this.applicationDate = applicationDate;
    }

}
