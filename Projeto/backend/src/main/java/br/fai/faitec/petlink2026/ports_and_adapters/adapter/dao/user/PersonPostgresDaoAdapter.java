package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user;

import br.fai.faitec.petlink2026.domain.user.PersonModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.PersonDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonPostgresDaoAdapter implements PersonDao {

    private final Connection connection;

    public PersonPostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(PersonModel entity) {
        final String userSql = "INSERT INTO user_model(email, full_name, password, phone) " +
                "VALUES(?,?,crypt(?, gen_salt('bf')),?);";

        final String personSql = "INSERT INTO person_model(cpf, user_id) VALUES(?,?);";

        try {
            connection.setAutoCommit(false);

            PreparedStatement userStatement = connection.prepareStatement(userSql, PreparedStatement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, entity.getEmail());
            userStatement.setString(2, entity.getFullname());
            userStatement.setString(3, entity.getPassword());
            userStatement.setString(4, entity.getPhone());
            userStatement.execute();

            ResultSet userKeys = userStatement.getGeneratedKeys();
            int userId = 0;
            if (userKeys.next()) {
                userId = userKeys.getInt(1);
            }
            userKeys.close();
            userStatement.close();

            PreparedStatement personStatement = connection.prepareStatement(personSql);
            personStatement.setString(1, entity.getCpf());
            personStatement.setInt(2, userId);
            personStatement.execute();
            personStatement.close();

            connection.commit();
            return userId;
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
        String sql = "DELETE FROM user_model WHERE id = ?;";

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
    public PersonModel readyById(int id) {
        final String sql = "SELECT u.id, u.email, u.full_name, u.password, u.phone, " +
                "p.cpf " +
                "FROM user_model u " +
                "JOIN person_model p ON p.user_id = u.id " +
                "WHERE u.id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            PersonModel personModel = null;

            if (resultSet.next()) {
                personModel = mapRow(resultSet);
            }

            resultSet.close();
            preparedStatement.close();

            return personModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PersonModel> readAll() {
        final List<PersonModel> entities = new ArrayList<>();

        final String sql = "SELECT u.id, u.email, u.full_name, u.password, u.phone, " +
                "p.cpf " +
                "FROM user_model u " +
                "JOIN person_model p ON p.user_id = u.id;";

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
    public void updateInformation(int id, PersonModel entity) {
        final String userSql = "UPDATE user_model SET " +
                "full_name = ?, " +
                "email = ?, " +
                "phone = ? " +
                "WHERE id = ?;";

        final String personSql = "UPDATE person_model SET cpf = ? WHERE user_id = ?;";

        try {
            connection.setAutoCommit(false);

            PreparedStatement userStatement = connection.prepareStatement(userSql);
            userStatement.setString(1, entity.getFullname());
            userStatement.setString(2, entity.getEmail());
            userStatement.setString(3, entity.getPhone());
            userStatement.setInt(4, id);
            userStatement.executeUpdate();
            userStatement.close();

            PreparedStatement personStatement = connection.prepareStatement(personSql);
            personStatement.setString(1, entity.getCpf());
            personStatement.setInt(2, id);
            personStatement.executeUpdate();
            personStatement.close();

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

    private PersonModel mapRow(ResultSet resultSet) throws SQLException {
        final PersonModel personModel = new PersonModel();
        personModel.setId(resultSet.getInt("id"));
        personModel.setEmail(resultSet.getString("email"));
        personModel.setFullname(resultSet.getString("full_name"));
        personModel.setPassword(resultSet.getString("password"));
        personModel.setPhone(resultSet.getString("phone"));
        personModel.setCpf(resultSet.getString("cpf"));
        return personModel;
    }
}
