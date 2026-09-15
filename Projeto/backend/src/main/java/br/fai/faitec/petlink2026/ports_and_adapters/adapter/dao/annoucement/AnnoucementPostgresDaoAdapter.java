package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.annoucement;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementModel;
import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementType;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.annoucement.AnnoucementDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnoucementPostgresDaoAdapter implements AnnoucementDao {

    private final Connection connection;

    public AnnoucementPostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(AnnoucementModel entity) {
        String sql = "INSERT INTO annoucement_model(title, description, publication_date, event_date, location, announcement_type, user_id) " +
                " VALUES(?,?,?,?,?,?,?); ";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setDate(3, entity.getPublicationDate());
            preparedStatement.setDate(4, entity.getEventDate());
            preparedStatement.setString(5, entity.getLocation());
            preparedStatement.setString(6, entity.getAnnoucementType().name());
            preparedStatement.setInt(7, entity.getIdCreator());

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
        String sql = "DELETE FROM annoucement_model " +
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
    public AnnoucementModel readyById(int id) {
        final String sql = "SELECT * FROM annoucement_model WHERE id = ? ;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String title = resultSet.getString("title");
                final String description = resultSet.getString("description");
                final Date publicationDate = resultSet.getDate("publication_date");
                final Date eventDate = resultSet.getDate("event_date");
                final String location = resultSet.getString("location");
                final int creatorId = resultSet.getInt("user_id");

                final String auxType = resultSet.getString("announcement_type");
                final AnnoucementType annoucementType = AnnoucementType.valueOf(auxType);

                final AnnoucementModel annoucementModel = new AnnoucementModel();
                annoucementModel.setId(entityId);
                annoucementModel.setTitle(title);
                annoucementModel.setDescription(description);
                annoucementModel.setPublicationDate(publicationDate);
                annoucementModel.setEventDate(eventDate);
                annoucementModel.setLocation(location);
                annoucementModel.setAnnoucementType(annoucementType);
                annoucementModel.setIdCreator(creatorId);

                preparedStatement.close();
                resultSet.close();

                return annoucementModel;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AnnoucementModel> readAll() {
        final List<AnnoucementModel> entities = new ArrayList<>();

        final String sql = "SELECT * FROM annoucement_model";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String title = resultSet.getString("title");
                final String description = resultSet.getString("description");
                final Date publicationDate = resultSet.getDate("publication_date");
                final Date eventDate = resultSet.getDate("event_date");
                final String location = resultSet.getString("location");
                final int creatorId = resultSet.getInt("user_id");

                final String auxType = resultSet.getString("announcement_type");
                final AnnoucementType annoucementType = AnnoucementType.valueOf(auxType);

                final AnnoucementModel annoucementModel = new AnnoucementModel();
                annoucementModel.setId(entityId);
                annoucementModel.setTitle(title);
                annoucementModel.setDescription(description);
                annoucementModel.setPublicationDate(publicationDate);
                annoucementModel.setEventDate(eventDate);
                annoucementModel.setLocation(location);
                annoucementModel.setAnnoucementType(annoucementType);
                annoucementModel.setIdCreator(creatorId);

                entities.add(annoucementModel);
            }

            resultSet.close();
            preparedStatement.close();

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}