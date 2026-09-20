package br.fai.faitec.petlink2026.dto.sale;

import java.util.List;

public class CalculateFarmAnimalSalePriceDto {

    private List<Integer> farmAnimalIds;
    private double pricePerArroba;

    public CalculateFarmAnimalSalePriceDto() {
    }

    public List<Integer> getFarmAnimalIds() {
        return farmAnimalIds;
    }

    public void setFarmAnimalIds(List<Integer> farmAnimalIds) {
        this.farmAnimalIds = farmAnimalIds;
    }

    public double getPricePerArroba() {
        return pricePerArroba;
    }

    public void setPricePerArroba(double pricePerArroba) {
        this.pricePerArroba = pricePerArroba;
    }
}