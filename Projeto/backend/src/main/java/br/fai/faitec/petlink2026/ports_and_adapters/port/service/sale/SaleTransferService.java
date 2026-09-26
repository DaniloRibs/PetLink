package br.fai.faitec.petlink2026.ports_and_adapters.port.service.sale;

import br.fai.faitec.petlink2026.domain.sale.FarmAnimalSaleModel;

import java.util.List;

public interface SaleTransferService {

    boolean requestSaleClosure(final int saleId, final String buyerEmail);

    boolean confirmSaleClosure(final int saleId, final int buyerId);

    boolean rejectSaleClosure(final int saleId, final int buyerId);

    List<FarmAnimalSaleModel> findPendingByBuyerId(final int buyerId);
}
