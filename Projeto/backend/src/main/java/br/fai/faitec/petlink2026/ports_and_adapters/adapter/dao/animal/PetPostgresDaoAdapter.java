package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.animal;

import br.fai.faitec.petlink2026.domain.animal.Specie;
import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.PetDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetPostgresDaoAdapter implements PetDao {

    private final Connection connection;

    public PetPostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(PetModel entity) {
        final String animalSql = "INSERT INTO animal_model(name, species, breed, birth_date, user_id, gender) " +
                "VALUES(?,?,?,?,?,?);";

        final String petSql = "INSERT INTO pet_model(for_adoption, animal_id) VALUES(?,?);";

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

            PreparedStatement petStatement = connection.prepareStatement(petSql);
            petStatement.setBoolean(1, entity.isForAdoption());
            petStatement.setInt(2, animalId);
            petStatement.execute();
            petStatement.close();

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
        // correspondente em pet_model via ON DELETE CASCADE.
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
    public PetModel readyById(int id) {
        final String sql = "SELECT a.id, a.name, a.species, a.breed, a.birth_date, a.user_id, a.gender, " +
                "p.for_adoption " +
                "FROM animal_model a " +
                "JOIN pet_model p ON p.animal_id = a.id " +
                "WHERE a.id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            PetModel petModel = null;

            if (resultSet.next()) {
                petModel = mapRow(resultSet);
            }

            resultSet.close();
            preparedStatement.close();

            return petModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PetModel> readAll() {
        final List<PetModel> entities = new ArrayList<>();

        final String sql = "SELECT a.id, a.name, a.species, a.breed, a.birth_date, a.user_id, a.gender, " +
                "p.for_adoption " +
                "FROM animal_model a " +
                "JOIN pet_model p ON p.animal_id = a.id;";

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
    public void updateInformation(int id, PetModel entity) {
        final String animalSql = "UPDATE animal_model SET " +
                "name = ?, " +
                "species = ?, " +
                "breed = ?, " +
                "birth_date = ?, " +
                "user_id = ?, " +
                "gender = ? " +
                "WHERE id = ?;";

        final String petSql = "UPDATE pet_model SET for_adoption = ? WHERE animal_id = ?;";

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

            PreparedStatement petStatement = connection.prepareStatement(petSql);
            petStatement.setBoolean(1, entity.isForAdoption());
            petStatement.setInt(2, id);
            petStatement.executeUpdate();
            petStatement.close();

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

    private PetModel mapRow(ResultSet resultSet) throws SQLException {
        final PetModel petModel = new PetModel();
        petModel.setId(resultSet.getInt("id"));
        petModel.setName(resultSet.getString("name"));
        petModel.setSpecies(Specie.valueOf(resultSet.getString("species")));
        petModel.setBreed(resultSet.getString("breed"));
        petModel.setBirthDate(resultSet.getDate("birth_date"));
        petModel.setOwnerId(resultSet.getInt("user_id"));
        petModel.setGender(resultSet.getString("gender"));
        petModel.setForAdoption(resultSet.getBoolean("for_adoption"));
        return petModel;
    }
}