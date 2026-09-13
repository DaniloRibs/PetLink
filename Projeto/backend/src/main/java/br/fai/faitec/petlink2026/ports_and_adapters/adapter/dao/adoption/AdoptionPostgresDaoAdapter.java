package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.adoption;

import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.adoption.AdoptionDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdoptionPostgresDaoAdapter implements AdoptionDao {

    private final Connection connection;

    public AdoptionPostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(AdoptionModel entity) {
        String sql = "INSERT INTO adoption_model(pet_id, owner_id, description, contact, adopted, publication_date) " +
                " VALUES(?,?,?,?,?,?); ";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, entity.getPetId());
            preparedStatement.setInt(2, entity.getOwnerId());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setString(4, entity.getContact());
            preparedStatement.setBoolean(5, entity.isAdopted());
            preparedStatement.setDate(6, entity.getPublicationDate());

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
        String sql = "DELETE FROM adoption_model " +
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
    public AdoptionModel readyById(int id) {
        final String sql = "SELECT * FROM adoption_model WHERE id = ? ;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final int petId = resultSet.getInt("pet_id");
                final int ownerId = resultSet.getInt("owner_id");
                final String description = resultSet.getString("description");
                final String contact = resultSet.getString("contact");
                final boolean adopted = resultSet.getBoolean("adopted");
                final Date publicationDate = resultSet.getDate("publication_date");

                final AdoptionModel adoptionModel = new AdoptionModel();
                adoptionModel.setId(entityId);
                adoptionModel.setPetId(petId);
                adoptionModel.setOwnerId(ownerId);
                adoptionModel.setDescription(description);
                adoptionModel.setContact(contact);
                adoptionModel.setAdopted(adopted);
                adoptionModel.setPublicationDate(publicationDate);

                preparedStatement.close();
                resultSet.close();

                return adoptionModel;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AdoptionModel> readAll() {
        final List<AdoptionModel> entities = new ArrayList<>();

        final String sql = "SELECT * FROM adoption_model";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final int petId = resultSet.getInt("pet_id");
                final int ownerId = resultSet.getInt("owner_id");
                final String description = resultSet.getString("description");
                final String contact = resultSet.getString("contact");
                final boolean adopted = resultSet.getBoolean("adopted");
                final Date publicationDate = resultSet.getDate("publication_date");

                final AdoptionModel adoptionModel = new AdoptionModel();
                adoptionModel.setId(entityId);
                adoptionModel.setPetId(petId);
                adoptionModel.setOwnerId(ownerId);
                adoptionModel.setDescription(description);
                adoptionModel.setContact(contact);
                adoptionModel.setAdopted(adopted);
                adoptionModel.setPublicationDate(publicationDate);

                entities.add(adoptionModel);
            }

            resultSet.close();
            preparedStatement.close();

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInformation(int id, AdoptionModel entity) {
        String sql = "UPDATE adoption_model SET " +
                "description = ?, " +
                "contact = ?, " +
                "adopted = ? " +
                "WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, entity.getDescription());
            preparedStatement.setString(2, entity.getContact());
            preparedStatement.setBoolean(3, entity.isAdopted());
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}