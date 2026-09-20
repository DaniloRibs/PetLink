package br.fai.faitec.petlink2026.ports_and_adapters.port.service.sale;

import java.util.List;

public interface CalculateSuggestedPrice {
    double calculateSuggestedPrice(List<Integer> farmAnimalIds, double pricePerArroba);
}
