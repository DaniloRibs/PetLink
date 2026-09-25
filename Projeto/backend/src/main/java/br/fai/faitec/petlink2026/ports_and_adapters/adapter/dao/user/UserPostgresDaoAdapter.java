package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user;


import br.fai.faitec.petlink2026.domain.user.EnterpriseModel;
import br.fai.faitec.petlink2026.domain.user.PersonModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserPostgresDaoAdapter implements UserDao {

    private static final String SELECT_USER = "SELECT u.id, u.email, u.full_name, u.password, u.phone, " +
            "p.id AS person_id, p.cpf, " +
            "e.id AS enterprise_id, e.cnpj " +
            "FROM user_model u " +
            "LEFT JOIN person_model p ON p.user_id = u.id " +
            "LEFT JOIN enterprise_model e ON e.user_id = u.id ";

    private final Connection connection;

    public UserPostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void remove(int id) {

        String sql = "DELETE FROM user_model " +
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
    public UserModel readyById(int id) {
        final String sql = SELECT_USER + "WHERE u.id = ? ;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            UserModel userModel = null;

            if (resultSet.next()) {
                userModel = mapRow(resultSet);
            }

            resultSet.close();
            preparedStatement.close();

            return userModel;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserModel> readAll() {
        final List<UserModel> entities = new ArrayList<>();

        final String sql = SELECT_USER + ";";

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
    public UserModel readByEmail(String email) {

        final String sql = SELECT_USER + "WHERE u.email = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            UserModel userModel = null;

            if (resultSet.next()) {
                userModel = mapRow(resultSet);
            }

            resultSet.close();
            preparedStatement.close();

            return userModel;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updatePassword(int id, String password) {
        String sql = "UPDATE user_model SET password = crypt(?, gen_salt('bf'))" +
                " WHERE id = ? ;";


        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, id);

            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserModel mapRow(ResultSet resultSet) throws SQLException {
        final int userId = resultSet.getInt("id");
        final UserModel userModel;

        if (resultSet.getObject("person_id") != null) {
            final PersonModel personModel = new PersonModel();
            personModel.setCpf(resultSet.getString("cpf"));
            userModel = personModel;
        } else if (resultSet.getObject("enterprise_id") != null) {
            final EnterpriseModel enterpriseModel = new EnterpriseModel();
            enterpriseModel.setCnpj(resultSet.getString("cnpj"));
            userModel = enterpriseModel;
        } else {
            throw new IllegalStateException("O usuário " + userId + " não está cadastrado como pessoa nem como empresa.");
        }

        userModel.setId(userId);
        userModel.setEmail(resultSet.getString("email"));
        userModel.setFullname(resultSet.getString("full_name"));
        userModel.setPassword(resultSet.getString("password"));
        userModel.setPhone(resultSet.getString("phone"));
        return userModel;
    }
}
