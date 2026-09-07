package br.fai.lds.projetolds2026.dto.vaccine;

import br.fai.lds.projetolds2026.domain.vaccine.VaccineModel;

import java.sql.Timestamp;

public class UpdateVaccineDto {


    private Timestamp applicationDate;
    private Timestamp expirationDate;
    private String name;
    private String description;
    private String lote;

    public VaccineModel toVaccineModel() {
        VaccineModel vaccineModel = new VaccineModel();
        vaccineModel.setName(name);
        vaccineModel.setLot(lote);
        vaccineModel.setDescription(description);
        vaccineModel.setExpirationDate(expirationDate);
        vaccineModel.setApplicationDate(applicationDate);

        return vaccineModel;
    }

    public Timestamp getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Timestamp applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
}
