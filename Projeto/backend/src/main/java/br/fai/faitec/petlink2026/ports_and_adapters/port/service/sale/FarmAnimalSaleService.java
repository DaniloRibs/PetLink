package br.fai.faitec.petlink2026.ports_and_adapters.port.service.sale;

import br.fai.faitec.petlink2026.domain.sale.FarmAnimalSaleModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.CrudService;

public interface FarmAnimalSaleService extends CrudService<FarmAnimalSaleModel>, CalculateSuggestedPrice {
}
