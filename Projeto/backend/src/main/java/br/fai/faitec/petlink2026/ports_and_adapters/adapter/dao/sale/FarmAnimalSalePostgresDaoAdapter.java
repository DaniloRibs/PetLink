package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.sale;

import br.fai.faitec.petlink2026.domain.sale.FarmAnimalSaleModel;
import br.fai.faitec.petlink2026.domain.sale.PriceType;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.sale.FarmAnimalSaleDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FarmAnimalSalePostgresDaoAdapter implements FarmAnimalSaleDao {

    private final Connection connection;

    public FarmAnimalSalePostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(FarmAnimalSaleModel entity) {
        final String saleSql = "INSERT INTO farm_animal_sale(description, price_type, price_per_arroba, price, user_id, contact) " +
                "VALUES(?,?,?,?,?,?);";

        final String itemSql = "INSERT INTO farm_animal_sale_item(farm_animal_sale_id, farm_animal_id) VALUES(?,?);";

        try {
            connection.setAutoCommit(false);

            PreparedStatement saleStatement = connection.prepareStatement(saleSql, PreparedStatement.RETURN_GENERATED_KEYS);
            saleStatement.setString(1, entity.getDescription());
            saleStatement.setString(2, entity.getPriceType().name());

            if (entity.getPricePerArroba() != null) {
                saleStatement.setDouble(3, entity.getPricePerArroba());
            } else {
                saleStatement.setNull(3, Types.NUMERIC);
            }

            saleStatement.setDouble(4, entity.getPrice());
            saleStatement.setInt(5, entity.getUserId());
            saleStatement.setString(6, entity.getContact());
            saleStatement.execute();

            ResultSet saleKeys = saleStatement.getGeneratedKeys();
            int saleId = 0;
            if (saleKeys.next()) {
                saleId = saleKeys.getInt(1);
            }
            saleKeys.close();
            saleStatement.close();

            insertSaleItems(saleId, entity.getFarmAnimalIds(), itemSql);

            connection.commit();
            return saleId;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private void insertSaleItems(int saleId, List<Integer> farmAnimalIds, String itemSql) throws SQLException {
        PreparedStatement itemStatement = connection.prepareStatement(itemSql);

        for (Integer farmAnimalId : farmAnimalIds) {
            itemStatement.setInt(1, saleId);
            itemStatement.setInt(2, farmAnimalId);
            itemStatement.addBatch();
        }

        itemStatement.executeBatch();
        itemStatement.close();
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM farm_animal_sale WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FarmAnimalSaleModel readyById(int id) {
        final String sql = "SELECT * FROM farm_animal_sale WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            FarmAnimalSaleModel saleModel = null;

            if (resultSet.next()) {
                saleModel = mapRow(resultSet);
            }

            resultSet.close();
            preparedStatement.close();

            if (saleModel != null) {
                saleModel.setFarmAnimalIds(fetchAnimalIds(id));
            }

            return saleModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FarmAnimalSaleModel> readAll() {
        final List<FarmAnimalSaleModel> entities = new ArrayList<>();

        final String sql = "SELECT * FROM farm_animal_sale;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                entities.add(mapRow(resultSet));
            }

            resultSet.close();
            preparedStatement.close();

            for (FarmAnimalSaleModel saleModel : entities) {
                saleModel.setFarmAnimalIds(fetchAnimalIds(saleModel.getId()));
            }

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInformation(int id, FarmAnimalSaleModel entity) {
        final String saleSql = "UPDATE farm_animal_sale SET " +
                "description = ?, " +
                "price_type = ?, " +
                "price_per_arroba = ?, " +
                "price = ?, " +
                "user_id = ?, " +
                "contact = ? " +
                "WHERE id = ?;";

        final String deleteItemsSql = "DELETE FROM farm_animal_sale_item WHERE farm_animal_sale_id = ?;";
        final String itemSql = "INSERT INTO farm_animal_sale_item(farm_animal_sale_id, farm_animal_id) VALUES(?,?);";

        try {
            connection.setAutoCommit(false);

            PreparedStatement saleStatement = connection.prepareStatement(saleSql);
            saleStatement.setString(1, entity.getDescription());
            saleStatement.setString(2, entity.getPriceType().name());

            if (entity.getPricePerArroba() != null) {
                saleStatement.setDouble(3, entity.getPricePerArroba());
            } else {
                saleStatement.setNull(3, Types.NUMERIC);
            }

            saleStatement.setDouble(4, entity.getPrice());
            saleStatement.setInt(5, entity.getUserId());
            saleStatement.setString(6, entity.getContact());
            saleStatement.setInt(7, id);
            saleStatement.executeUpdate();
            saleStatement.close();

            PreparedStatement deleteItemsStatement = connection.prepareStatement(deleteItemsSql);
            deleteItemsStatement.setInt(1, id);
            deleteItemsStatement.executeUpdate();
            deleteItemsStatement.close();

            insertSaleItems(id, entity.getFarmAnimalIds(), itemSql);

            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private List<Integer> fetchAnimalIds(int saleId) throws SQLException {
        final List<Integer> animalIds = new ArrayList<>();

        final String sql = "SELECT farm_animal_id FROM farm_animal_sale_item WHERE farm_animal_sale_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, saleId);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            animalIds.add(resultSet.getInt("farm_animal_id"));
        }

        resultSet.close();
        preparedStatement.close();

        return animalIds;
    }

    private FarmAnimalSaleModel mapRow(ResultSet resultSet) throws SQLException {
        final FarmAnimalSaleModel saleModel = new FarmAnimalSaleModel();
        saleModel.setId(resultSet.getInt("id"));
        saleModel.setDescription(resultSet.getString("description"));
        saleModel.setPriceType(PriceType.valueOf(resultSet.getString("price_type")));

        final double pricePerArroba = resultSet.getDouble("price_per_arroba");
        saleModel.setPricePerArroba(resultSet.wasNull() ? null : pricePerArroba);

        saleModel.setPrice(resultSet.getDouble("price"));
        saleModel.setUserId(resultSet.getInt("user_id"));
        saleModel.setContact(resultSet.getString("contact"));

        return saleModel;
    }
}