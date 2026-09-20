package br.fai.faitec.petlink2026.domain.sale;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;

import java.sql.Date;
import java.util.List;

public class FarmAnimalSaleModel {

    public static final double KG_PER_ARROBA = 15.0;

    private int id;
    private String description;
    private List<Integer> farmAnimalIds;
    private PriceType priceType;
    private Double pricePerArroba;
    private double price;
    private int userId;
    private String contact;

    public static double calculateSuggestedPriceByArroba(List<FarmAnimalModel> farmAnimals, double pricePerArroba) {

        if (farmAnimals == null || farmAnimals.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar ao menos um animal para calcular o valor.");
        }

        if (pricePerArroba <= 0) {
            throw new IllegalArgumentException("O valor da arroba deve ser maior que zero.");
        }

        double totalWeight = 0;
        for (FarmAnimalModel farmAnimal : farmAnimals) {
            totalWeight += farmAnimal.getWeight();
        }

        double totalArrobas = totalWeight / KG_PER_ARROBA;

        return totalArrobas * pricePerArroba;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getFarmAnimalIds() {
        return farmAnimalIds;
    }

    public void setFarmAnimalIds(List<Integer> farmAnimalIds) {
        this.farmAnimalIds = farmAnimalIds;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public Double getPricePerArroba() {
        return pricePerArroba;
    }

    public void setPricePerArroba(Double pricePerArroba) {
        this.pricePerArroba = pricePerArroba;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isGroupSale() {
        return farmAnimalIds != null && farmAnimalIds.size() > 1;
    }

}