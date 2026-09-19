package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.animal;

import br.fai.faitec.petlink2026.domain.animal.Specie;
import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.FarmAnimalDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmAnimalPostgresDaoAdapter implements FarmAnimalDao {

    private final Connection connection;

    public FarmAnimalPostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(FarmAnimalModel entity) {
        final String animalSql = "INSERT INTO animal_model(name, species, breed, birth_date, user_id, gender) " +
                "VALUES(?,?,?,?,?,?);";

        final String farmAnimalSql = "INSERT INTO farm_animal_model(identifier, for_sell, weight, animal_id) " +
                "VALUES(?,?,?,?);";

        try {
            connection.setAutoCommit(false);

            PreparedStatement animalStatement = connection.prepareStatement(animalSql, PreparedStatement.RETURN_GENERATED_KEYS);
            animalStatement.setString(1, entity.getName());
            animalStatement.setString(2, entity.getSpecies().name());
            animalStatement.setString(3, entity.getBreed());
            animalStatement.setDate(4, new Date(entity.getBirthDate().getTime()));
            animalStatement.setInt(5, entity.getOwnerId());
            animalStatement.setString(6, entity.getGender());
            animalStatement.execute();

            ResultSet animalKeys = animalStatement.getGeneratedKeys();
            int animalId = 0;
            if (animalKeys.next()) {
                animalId = animalKeys.getInt(1);
            }
            animalKeys.close();
            animalStatement.close();

            PreparedStatement farmAnimalStatement = connection.prepareStatement(farmAnimalSql);
            farmAnimalStatement.setString(1, entity.getIdentify());
            farmAnimalStatement.setBoolean(2, entity.isForSell());
            farmAnimalStatement.setDouble(3, entity.getWeight());
            farmAnimalStatement.setInt(4, animalId);
            farmAnimalStatement.execute();
            farmAnimalStatement.close();

            connection.commit();
            return animalId;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(int id) {
        // animal_model é o "pai": remover aqui apaga também a linha
        // correspondente em farm_animal_model via ON DELETE CASCADE.
        String sql = "DELETE FROM animal_model WHERE id = ?;";

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
    public FarmAnimalModel readyById(int id) {
        final String sql = "SELECT a.id, a.name, a.species, a.breed, a.birth_date, a.user_id, a.gender, " +
                "f.identifier, f.for_sell, f.weight " +
                "FROM animal_model a " +
                "JOIN farm_animal_model f ON f.animal_id = a.id " +
                "WHERE a.id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            FarmAnimalModel farmAnimalModel = null;

            if (resultSet.next()) {
                farmAnimalModel = mapRow(resultSet);
            }

            resultSet.close();
            preparedStatement.close();

            return farmAnimalModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FarmAnimalModel> readAll() {
        final List<FarmAnimalModel> entities = new ArrayList<>();

        final String sql = "SELECT a.id, a.name, a.species, a.breed, a.birth_date, a.user_id, a.gender, " +
                "f.identifier, f.for_sell, f.weight " +
                "FROM animal_model a " +
                "JOIN farm_animal_model f ON f.animal_id = a.id;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                entities.add(mapRow(resultSet));
            }

            resultSet.close();
            preparedStatement.close();

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInformation(int id, FarmAnimalModel entity) {
        final String animalSql = "UPDATE animal_model SET " +
                "name = ?, " +
                "species = ?, " +
                "breed = ?, " +
                "birth_date = ?, " +
                "user_id = ?, " +
                "gender = ? " +
                "WHERE id = ?;";

        final String farmAnimalSql = "UPDATE farm_animal_model SET " +
                "identifier = ?, " +
                "for_sell = ?, " +
                "weight = ? " +
                "WHERE animal_id = ?;";

        try {
            connection.setAutoCommit(false);

            PreparedStatement animalStatement = connection.prepareStatement(animalSql);
            animalStatement.setString(1, entity.getName());
            animalStatement.setString(2, entity.getSpecies().name());
            animalStatement.setString(3, entity.getBreed());
            animalStatement.setDate(4, new Date(entity.getBirthDate().getTime()));
            animalStatement.setInt(5, entity.getOwnerId());
            animalStatement.setString(6, entity.getGender());
            animalStatement.setInt(7, id);
            animalStatement.executeUpdate();
            animalStatement.close();

            PreparedStatement farmAnimalStatement = connection.prepareStatement(farmAnimalSql);
            farmAnimalStatement.setString(1, entity.getIdentify());
            farmAnimalStatement.setBoolean(2, entity.isForSell());
            farmAnimalStatement.setDouble(3, entity.getWeight());
            farmAnimalStatement.setInt(4, id);
            farmAnimalStatement.executeUpdate();
            farmAnimalStatement.close();

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

    private FarmAnimalModel mapRow(ResultSet resultSet) throws SQLException {
        final FarmAnimalModel farmAnimalModel = new FarmAnimalModel();
        farmAnimalModel.setId(resultSet.getInt("id"));
        farmAnimalModel.setName(resultSet.getString("name"));
        farmAnimalModel.setSpecies(Specie.valueOf(resultSet.getString("species")));
        farmAnimalModel.setBreed(resultSet.getString("breed"));
        farmAnimalModel.setBirthDate(resultSet.getDate("birth_date"));
        farmAnimalModel.setOwnerId(resultSet.getInt("user_id"));
        farmAnimalModel.setGender(resultSet.getString("gender"));
        farmAnimalModel.setIdentify(resultSet.getString("identifier"));
        farmAnimalModel.setForSell(resultSet.getBoolean("for_sell"));
        farmAnimalModel.setWeight(resultSet.getDouble("weight"));
        return farmAnimalModel;
    }
}