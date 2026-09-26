package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.sale;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.sale.FarmAnimalSaleModel;
import br.fai.faitec.petlink2026.domain.sale.SaleStatus;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.sale.FarmAnimalSaleDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.FarmAnimalService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.sale.SaleTransferService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleTransferServiceAdapter implements SaleTransferService {

    @Autowired
    private FarmAnimalSaleDao farmAnimalSaleDao;

    @Autowired
    private FarmAnimalService farmAnimalService;

    @Autowired
    private UserService userService;

    @Override
    public boolean requestSaleClosure(final int saleId, final String buyerEmail) {
        if (isIdInvalid(saleId) || buyerEmail == null || buyerEmail.isBlank()) {
            return false;
        }

        final FarmAnimalSaleModel sale = farmAnimalSaleDao.readyById(saleId);

        if (sale == null) {
            return false;
        }

        if (sale.getSaleStatus() == SaleStatus.ACCEPTED || sale.getSaleStatus() == SaleStatus.PENDING) {
            return false;
        }

        final UserModel buyer = userService.findByEmail(buyerEmail.trim());

        if (buyer == null || buyer.getId() == sale.getUserId()) {
            return false;
        }

        if (sale.getFarmAnimalIds() == null || sale.getFarmAnimalIds().isEmpty()) {
            return false;
        }

        for (final Integer farmAnimalId : sale.getFarmAnimalIds()) {
            final FarmAnimalModel farmAnimal = farmAnimalService.findById(farmAnimalId);

            if (farmAnimal == null || farmAnimal.getOwnerId() != sale.getUserId() || !farmAnimal.isForSell()) {
                return false;
            }
        }

        sale.setBuyerId(buyer.getId());
        sale.setSaleStatus(SaleStatus.PENDING);

        farmAnimalSaleDao.updateInformation(saleId, sale);

        return true;
    }

    @Override
    public boolean confirmSaleClosure(final int saleId, final int buyerId) {
        final FarmAnimalSaleModel sale = findPendingSale(saleId, buyerId);

        if (sale == null) {
            return false;
        }

        for (final Integer farmAnimalId : sale.getFarmAnimalIds()) {
            final boolean transferred = farmAnimalService.updateOwner(farmAnimalId, sale.getUserId(), buyerId);

            if (!transferred) {
                return false;
            }
        }

        sale.setSaleStatus(SaleStatus.ACCEPTED);
        farmAnimalSaleDao.updateInformation(saleId, sale);

        return true;
    }

    @Override
    public boolean rejectSaleClosure(final int saleId, final int buyerId) {
        final FarmAnimalSaleModel sale = findPendingSale(saleId, buyerId);

        if (sale == null) {
            return false;
        }

        sale.setSaleStatus(SaleStatus.REJECTED);

        farmAnimalSaleDao.updateInformation(saleId, sale);

        return true;
    }

    @Override
    public List<FarmAnimalSaleModel> findPendingByBuyerId(final int buyerId) {
        final List<FarmAnimalSaleModel> pending = new ArrayList<>();

        if (isIdInvalid(buyerId)) {
            return pending;
        }

        for (final FarmAnimalSaleModel sale : farmAnimalSaleDao.readAll()) {
            if (sale.getSaleStatus() == SaleStatus.PENDING && sale.getBuyerId() == buyerId) {
                pending.add(sale);
            }
        }

        return pending;
    }

    private FarmAnimalSaleModel findPendingSale(final int saleId, final int buyerId) {
        if (isIdInvalid(saleId) || isIdInvalid(buyerId)) {
            return null;
        }

        final FarmAnimalSaleModel sale = farmAnimalSaleDao.readyById(saleId);

        if (sale == null || sale.getSaleStatus() != SaleStatus.PENDING) {
            return null;
        }

        if (sale.getBuyerId() != buyerId) {
            return null;
        }

        return sale;
    }

    private boolean isIdInvalid(final int id) {
        return id <= 0;
    }
}
