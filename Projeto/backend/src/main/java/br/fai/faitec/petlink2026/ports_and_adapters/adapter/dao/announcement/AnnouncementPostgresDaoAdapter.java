package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.announcement;

import br.fai.faitec.petlink2026.domain.announcement.AnnouncementModel;
import br.fai.faitec.petlink2026.domain.announcement.AnnouncementType;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.announcement.AnnouncementDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementPostgresDaoAdapter implements AnnouncementDao {

    private final Connection connection;

    public AnnouncementPostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(AnnouncementModel entity) {
        String sql = "INSERT INTO announcement_model(title, description, event_date, location, announcement_type, user_id) " +
                " VALUES(?,?,?,?,?,?); ";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setDate(3, entity.getEventDate());
            preparedStatement.setString(4, entity.getLocation());
            preparedStatement.setString(5, entity.getAnnouncementType().name());
            preparedStatement.setInt(6, entity.getIdCreator());

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
        String sql = "DELETE FROM announcement_model " +
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
    public AnnouncementModel readyById(int id) {
        final String sql = "SELECT * FROM announcement_model WHERE id = ? ;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String title = resultSet.getString("title");
                final String description = resultSet.getString("description");
                final Date eventDate = resultSet.getDate("event_date");
                final String location = resultSet.getString("location");
                final int creatorId = resultSet.getInt("user_id");

                final String auxType = resultSet.getString("announcement_type");
                final AnnouncementType announcementType = AnnouncementType.valueOf(auxType);

                final AnnouncementModel announcementModel = new AnnouncementModel();
                announcementModel.setId(entityId);
                announcementModel.setTitle(title);
                announcementModel.setDescription(description);
                announcementModel.setEventDate(eventDate);
                announcementModel.setLocation(location);
                announcementModel.setAnnouncementType(announcementType);
                announcementModel.setIdCreator(creatorId);

                preparedStatement.close();
                resultSet.close();

                return announcementModel;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AnnouncementModel> readAll() {
        final List<AnnouncementModel> entities = new ArrayList<>();

        final String sql = "SELECT * FROM announcement_model";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String title = resultSet.getString("title");
                final String description = resultSet.getString("description");
                final Date eventDate = resultSet.getDate("event_date");
                final String location = resultSet.getString("location");
                final int creatorId = resultSet.getInt("user_id");

                final String auxType = resultSet.getString("announcement_type");
                final AnnouncementType announcementType = AnnouncementType.valueOf(auxType);

                final AnnouncementModel announcementModel = new AnnouncementModel();
                announcementModel.setId(entityId);
                announcementModel.setTitle(title);
                announcementModel.setDescription(description);
                announcementModel.setEventDate(eventDate);
                announcementModel.setLocation(location);
                announcementModel.setAnnouncementType(announcementType);
                announcementModel.setIdCreator(creatorId);

                entities.add(announcementModel);
            }

            resultSet.close();
            preparedStatement.close();

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInformation(int id, AnnouncementModel entity) {
        String sql = "UPDATE announcement_model SET " +
                "title = ?, " +
                "description = ?, " +
                "event_date = ?, " +
                "location = ?, " +
                "announcement_type = ? " +
                "WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setDate(3, entity.getEventDate());
            preparedStatement.setString(4, entity.getLocation());
            preparedStatement.setString(5, entity.getAnnouncementType().name());
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}