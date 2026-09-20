package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.vaccine;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.vaccine.VaccineDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VaccinePostgresDaoAdapter implements VaccineDao {

    private final Connection connection;

    public VaccinePostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(VaccineModel entity) {
        String sql = "INSERT INTO vaccine_model(application_date, expiration_date, name, description, batch, animal_id) " +
                " VALUES(?,?,?,?,?,?); ";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setDate(1, entity.getApplicationDate());
            preparedStatement.setDate(2, entity.getExpirationDate());
            preparedStatement.setString(3, entity.getName());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setString(5, entity.getBatch());
            preparedStatement.setInt(6, entity.getPetId());

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
        String sql = "DELETE FROM vaccine_model " +
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
    public VaccineModel readyById(int id) {
        final String sql = "SELECT * FROM vaccine_model WHERE id = ? ;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String name = resultSet.getString("name");
                final String description = resultSet.getString("description");
                final String batch = resultSet.getString("batch");
                final Date applicationDate = resultSet.getDate("application_date");
                final Date expirationDate = resultSet.getDate("expiration_date");
                final int petId = resultSet.getInt("animal_id");

                final VaccineModel vaccineModel = new VaccineModel();
                vaccineModel.setId(entityId);
                vaccineModel.setName(name);
                vaccineModel.setDescription(description);
                vaccineModel.setBatch(batch);
                vaccineModel.setApplicationDate(applicationDate);
                vaccineModel.setExpirationDate(expirationDate);
                vaccineModel.setPetId(petId);

                preparedStatement.close();
                resultSet.close();

                return vaccineModel;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VaccineModel> readAll() {
        final List<VaccineModel> entities = new ArrayList<>();

        final String sql = "SELECT * FROM vaccine_model";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String name = resultSet.getString("name");
                final String description = resultSet.getString("description");
                final String batch = resultSet.getString("batch");
                final Date applicationDate = resultSet.getDate("application_date");
                final Date expirationDate = resultSet.getDate("expiration_date");
                final int petId = resultSet.getInt("animal_id");

                final VaccineModel vaccineModel = new VaccineModel();
                vaccineModel.setId(entityId);
                vaccineModel.setName(name);
                vaccineModel.setDescription(description);
                vaccineModel.setBatch(batch);
                vaccineModel.setApplicationDate(applicationDate);
                vaccineModel.setExpirationDate(expirationDate);
                vaccineModel.setPetId(petId);

                entities.add(vaccineModel);
            }

            resultSet.close();
            preparedStatement.close();

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInformation(int id, VaccineModel entity) {
        String sql = "UPDATE vaccine_model SET " +
                "application_date = ?, " +
                "expiration_date = ?, " +
                "name = ?, " +
                "description = ?, " +
                "batch = ? " +
                "WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setDate(1, entity.getApplicationDate());
            preparedStatement.setDate(2, entity.getExpirationDate());
            preparedStatement.setString(3, entity.getName());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setString(5, entity.getBatch());
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}