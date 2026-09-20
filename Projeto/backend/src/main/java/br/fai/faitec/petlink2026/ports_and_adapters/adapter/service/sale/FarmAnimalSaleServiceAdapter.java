package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.sale;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.sale.FarmAnimalSaleModel;
import br.fai.faitec.petlink2026.domain.sale.PriceType;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.FarmAnimalDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.sale.FarmAnimalSaleDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.sale.FarmAnimalSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FarmAnimalSaleServiceAdapter implements FarmAnimalSaleService {

    @Autowired
    private FarmAnimalSaleDao farmAnimalSaleDao;
    @Autowired
    private FarmAnimalDao farmAnimalDao;
    @Autowired
    private UserDao userDao;

    @Override
    public int create(FarmAnimalSaleModel farmAnimalSaleModel) {

        if (farmAnimalSaleModel == null) {
            return 0;
        }

        if (farmAnimalSaleModel.getDescription() == null || farmAnimalSaleModel.getDescription().isEmpty()) {
            return 0;
        }

        if (farmAnimalSaleModel.getContact() == null || farmAnimalSaleModel.getContact().isEmpty()) {
            return 0;
        }

        if (farmAnimalSaleModel.getPriceType() == null) {
            return 0;
        }

        if (farmAnimalSaleModel.getPrice() <= 0) {
            return 0;
        }

        if (farmAnimalSaleModel.getPriceType() == PriceType.PER_ARROBA
                && (farmAnimalSaleModel.getPricePerArroba() == null || farmAnimalSaleModel.getPricePerArroba() <= 0)) {
            return 0;
        }

        if (isIdInvalid(farmAnimalSaleModel.getUserId())) {
            return 0;
        }

        UserModel seller = userDao.readyById(farmAnimalSaleModel.getUserId());

        if (seller == null) {
            return 0;
        }

        List<FarmAnimalModel> farmAnimals = resolveFarmAnimals(farmAnimalSaleModel.getFarmAnimalIds());

        if (farmAnimals == null) {
            return 0;
        }

        for (FarmAnimalModel farmAnimal : farmAnimals) {
            if (farmAnimal.getOwnerId() != farmAnimalSaleModel.getUserId()) {
                return 0;
            }
        }

        return farmAnimalSaleDao.add(farmAnimalSaleModel);
    }

    @Override
    public void delete(int id) {
        if (isIdInvalid(id)) {
            return;
        }

        FarmAnimalSaleModel farmAnimalSaleModel = farmAnimalSaleDao.readyById(id);

        if (farmAnimalSaleModel == null) {
            return;
        }

        farmAnimalSaleDao.remove(id);
    }

    @Override
    public FarmAnimalSaleModel findById(int id) {
        if (isIdInvalid(id)) {
            return null;
        }

        return farmAnimalSaleDao.readyById(id);
    }

    @Override
    public List<FarmAnimalSaleModel> findAll() {
        return farmAnimalSaleDao.readAll();
    }

    @Override
    public boolean update(int id, FarmAnimalSaleModel farmAnimalSaleModel) {

        if (isIdInvalid(id) || farmAnimalSaleModel == null) {
            return false;
        }

        FarmAnimalSaleModel dataToUpdate = farmAnimalSaleDao.readyById(id);

        if (dataToUpdate == null) {
            return false;
        }

        if (farmAnimalSaleModel.getDescription() == null || farmAnimalSaleModel.getDescription().isEmpty()) {
            return false;
        }

        if (farmAnimalSaleModel.getContact() == null || farmAnimalSaleModel.getContact().isEmpty()) {
            return false;
        }

        if (farmAnimalSaleModel.getPriceType() == null) {
            return false;
        }

        if (farmAnimalSaleModel.getPrice() <= 0) {
            return false;
        }

        if (farmAnimalSaleModel.getPriceType() == PriceType.PER_ARROBA
                && (farmAnimalSaleModel.getPricePerArroba() == null || farmAnimalSaleModel.getPricePerArroba() <= 0)) {
            return false;
        }

        List<FarmAnimalModel> farmAnimals = resolveFarmAnimals(farmAnimalSaleModel.getFarmAnimalIds());

        if (farmAnimals == null) {
            return false;
        }

        for (FarmAnimalModel farmAnimal : farmAnimals) {
            if (farmAnimal.getOwnerId() != dataToUpdate.getUserId()) {
                return false;
            }
        }

        dataToUpdate.setDescription(farmAnimalSaleModel.getDescription());
        dataToUpdate.setContact(farmAnimalSaleModel.getContact());
        dataToUpdate.setPriceType(farmAnimalSaleModel.getPriceType());
        dataToUpdate.setPricePerArroba(farmAnimalSaleModel.getPricePerArroba());
        dataToUpdate.setPrice(farmAnimalSaleModel.getPrice());
        dataToUpdate.setFarmAnimalIds(farmAnimalSaleModel.getFarmAnimalIds());

        farmAnimalSaleDao.updateInformation(id, dataToUpdate);

        return true;
    }

    @Override
    public double calculateSuggestedPrice(List<Integer> farmAnimalIds, double pricePerArroba) {

        if (farmAnimalIds == null || farmAnimalIds.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar ao menos um animal para calcular o valor.");
        }

        if (pricePerArroba <= 0) {
            throw new IllegalArgumentException("O valor da arroba deve ser maior que zero.");
        }

        List<FarmAnimalModel> farmAnimals = resolveFarmAnimals(farmAnimalIds);

        if (farmAnimals == null) {
            throw new IllegalArgumentException("Um ou mais animais informados não foram encontrados.");
        }

        return FarmAnimalSaleModel.calculateSuggestedPriceByArroba(farmAnimals, pricePerArroba);
    }

    private List<FarmAnimalModel> resolveFarmAnimals(List<Integer> farmAnimalIds) {

        if (farmAnimalIds == null || farmAnimalIds.isEmpty()) {
            return null;
        }

        List<FarmAnimalModel> farmAnimals = new ArrayList<>();

        for (Integer farmAnimalId : farmAnimalIds) {
            FarmAnimalModel farmAnimal = farmAnimalDao.readyById(farmAnimalId);

            if (farmAnimal == null) {
                return null;
            }

            farmAnimals.add(farmAnimal);
        }

        return farmAnimals;
    }

    boolean isIdInvalid(int id) {
        return id < 0;
    }
}