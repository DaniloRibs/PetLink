package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.pet;

import br.fai.faitec.petlink2026.domain.pet.Specie;
import br.fai.faitec.petlink2026.domain.pet.PetModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.pet.PetDao;

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
        String sql = "INSERT INTO pet_model(name, specie, breed, birth_date, user_id, for_adoption) " +
                " VALUES(?,?,?,?,?,?); ";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSpecie().name());
            preparedStatement.setString(3, entity.getBreed());
            preparedStatement.setDate(4, new Date(entity.getBirthDate().getTime()));
            preparedStatement.setInt(5, entity.getOwnerId());
            preparedStatement.setBoolean(6, entity.isForAdoption());

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();
            int id = 0;

            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            connection.commit();
            resultSet.close();
            preparedStatement.close();
            return id;
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
        String sql = "DELETE FROM pet_model " +
                "WHERE id = ? ;";

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
        final String sql = "SELECT * FROM pet_model WHERE id = ? ;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String name = resultSet.getString("name");
                final String breed = resultSet.getString("breed");
                final Date birthDate = resultSet.getDate("birth_date");
                final int userId = resultSet.getInt("user_id");

                final String auxSpecies = resultSet.getString("specie");
                final Specie specie = Specie.valueOf(auxSpecies);

                final boolean forAdoption = resultSet.getBoolean("for_adoption");

                final PetModel petModel = new PetModel();
                petModel.setId(entityId);
                petModel.setName(name);
                petModel.setSpecie(specie);
                petModel.setBreed(breed);
                petModel.setBirthDate(birthDate);
                petModel.setOwnerId(userId);
                petModel.setForAdoption(forAdoption);

                preparedStatement.close();
                resultSet.close();

                return petModel;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PetModel> readAll() {
        final List<PetModel> entities = new ArrayList<>();

        final String sql = "SELECT * FROM pet_model";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String name = resultSet.getString("name");
                final String breed = resultSet.getString("breed");
                final Date birthDate = resultSet.getDate("birth_date");
                final int userId = resultSet.getInt("user_id");

                final String auxSpecies = resultSet.getString("specie");
                final Specie specie = Specie.valueOf(auxSpecies);
                final boolean forAdoption = resultSet.getBoolean("for_adoption");

                final PetModel petModel = new PetModel();
                petModel.setId(entityId);
                petModel.setName(name);
                petModel.setSpecie(specie);
                petModel.setBreed(breed);
                petModel.setBirthDate(birthDate);
                petModel.setOwnerId(userId);
                petModel.setForAdoption(forAdoption);
                entities.add(petModel);
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
        String sql = "UPDATE pet_model SET " +
                "name = ?, " +
                "specie = ?, " +
                "breed = ?, " +
                "birth_date = ?, " +
                "user_id = ? " +
                "for_adoption = ?" +
                "WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSpecie().name());
            preparedStatement.setString(3, entity.getBreed());
            preparedStatement.setDate(4, entity.getBirthDate());
            preparedStatement.setInt(5, entity.getOwnerId());
            preparedStatement.setBoolean(6, entity.isForAdoption());
            preparedStatement.setInt(7, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}