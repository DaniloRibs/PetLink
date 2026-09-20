package br.fai.faitec.petlink2026.dto.sale;

import br.fai.faitec.petlink2026.domain.sale.FarmAnimalSaleModel;
import br.fai.faitec.petlink2026.domain.sale.PriceType;

import java.util.List;

public class CreateFarmAnimalSaleDto {

    private String description;
    private List<Integer> farmAnimalIds;
    private PriceType priceType;
    private Double pricePerArroba;
    private Double price;
    private int userId;
    private String contact;

    public FarmAnimalSaleModel toFarmAnimalSaleModel() {

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("A descrição da venda é obrigatória.");
        }

        if (farmAnimalIds == null || farmAnimalIds.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar ao menos um animal para a venda.");
        }

        if (priceType == null) {
            throw new IllegalArgumentException("O tipo de precificação (manual ou por arroba) é obrigatório.");
        }

        if (priceType == PriceType.PER_ARROBA && (pricePerArroba == null || pricePerArroba <= 0)) {
            throw new IllegalArgumentException("O valor da arroba é obrigatório e deve ser maior que zero para vendas por arroba.");
        }

        if (price == null || price <= 0) {
            throw new IllegalArgumentException("O preço final da venda é obrigatório. Calcule e aprove o valor antes de confirmar a venda.");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("O usuário responsável pela venda é obrigatório.");
        }

        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("O contato para a venda é obrigatório.");
        }

        final FarmAnimalSaleModel farmAnimalSaleModel = new FarmAnimalSaleModel();
        farmAnimalSaleModel.setDescription(description);
        farmAnimalSaleModel.setFarmAnimalIds(farmAnimalIds);
        farmAnimalSaleModel.setPriceType(priceType);
        farmAnimalSaleModel.setPricePerArroba(pricePerArroba);
        farmAnimalSaleModel.setPrice(price);
        farmAnimalSaleModel.setUserId(userId);
        farmAnimalSaleModel.setContact(contact);

        return farmAnimalSaleModel;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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

}