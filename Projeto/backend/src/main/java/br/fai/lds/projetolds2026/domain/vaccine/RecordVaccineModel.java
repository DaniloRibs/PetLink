package br.fai.lds.projetolds2026.domain.vaccine;

import java.sql.Timestamp;

public class RecordVaccineModel {
    private int id;
    private Timestamp aplicationDate;
    private VaccineModel vaccine;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getAplicationDate() {
        return aplicationDate;
    }

    public void setAplicationDate(Timestamp aplicationDate) {
        this.aplicationDate = aplicationDate;
    }

    public VaccineModel getVaccine() {
        return vaccine;
    }

    public void setVaccine(VaccineModel vaccine) {
        this.vaccine = vaccine;
    }
}
