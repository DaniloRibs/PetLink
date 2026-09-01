package br.fai.lds.projetolds2026.domain.pet;

import br.fai.lds.projetolds2026.domain.vaccine.RecordVaccineModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PetModel {

    private int id;
    private String name;
    private Especie especie;
    private String race;
    private Timestamp anoNasc;
    private int idOwner;
    private List<RecordVaccineModel> vaccines = new ArrayList<>();

    public List<RecordVaccineModel> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<RecordVaccineModel> vaccines) {
        this.vaccines = vaccines;
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

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }
}
