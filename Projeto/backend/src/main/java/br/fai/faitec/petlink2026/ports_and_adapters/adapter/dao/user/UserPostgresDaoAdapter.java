package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user;


import br.fai.faitec.petlink2026.domain.user.AccountType;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPostgresDaoAdapter implements UserDao {

    private final Connection connection;

    public UserPostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }


    @Override
    public int add(UserModel userModel) {
        String sql = "INSERT INTO user_model(password, full_name, email, account_type, phone, document) " +
                " VALUES(?,?,?,?,?,?); ";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, userModel.getPassword());
            preparedStatement.setString(2, userModel.getFullname());
            preparedStatement.setString(3, userModel.getEmail());
            preparedStatement.setString(4, userModel.getAccountType().name());
            preparedStatement.setString(5, userModel.getPhone());
            preparedStatement.setString(6, userModel.getDocument());


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
        final String sql = "SELECT * FROM user_model WHERE id = ? ;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final int entityID = resultSet.getInt("id");
                final String fullname = resultSet.getString("full_name");
                final String email = resultSet.getString("email");
                final String password = resultSet.getString("password");
                final String document = resultSet.getString("document");
                final String phone = resultSet.getString("phone");


                final String auxAccountType = resultSet.getString("account_type");
                final AccountType accountType = AccountType.valueOf(auxAccountType);

                final UserModel userModel = new UserModel();
                userModel.setId(entityID);
                userModel.setFullname(fullname);
                userModel.setEmail(email);
                userModel.setPassword(password);
                userModel.setPhone(phone);
                userModel.setDocument(document);
                userModel.setAccountType(accountType);

                preparedStatement.close();
                resultSet.close();

                return userModel;

            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserModel> readAll() {
        final List<UserModel> entities = new ArrayList<>();

        final String sql = "SELECT * FROM user_model";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String fullname = resultSet.getString("full_name");
                final String email = resultSet.getString("email");
                final String password = resultSet.getString("password");
                final String document = resultSet.getString("document");
                final String phone = resultSet.getString("phone");


                final String auxAccountType = resultSet.getString("account_type");
                final AccountType accountType = AccountType.valueOf(auxAccountType);

                final UserModel userModel = new UserModel();
                userModel.setId(entityId);
                userModel.setFullname(fullname);
                userModel.setEmail(email);
                userModel.setPassword(password);
                userModel.setPhone(phone);
                userModel.setDocument(document);
                userModel.setAccountType(accountType);


                entities.add(userModel);

            }

            resultSet.close();
            preparedStatement.close();

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

    }


    @Override
    public void updateInformation(int id, UserModel entity) {

        String sql = "UPDATE user_model SET " +
                "full_name = ?, " +
                "email = ?, " +
                "phone = ?, " +
                "document = ? " +
                "WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, entity.getFullname());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPhone());
            preparedStatement.setString(4, entity.getDocument());
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserModel readByEmail(String email) {

        final String sql = "SELECT * FROM user_model WHERE email = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final int entityId = resultSet.getInt("id");
                final String fullname = resultSet.getString("full_name");
                final String password = resultSet.getString("password");
                final String document = resultSet.getString("document");
                final String phone = resultSet.getString("phone");


                final String auxAccountType = resultSet.getString("account_type");
                final AccountType accountType = AccountType.valueOf(auxAccountType);


                UserModel userModel = new UserModel();
                userModel.setId(entityId);
                userModel.setFullname(fullname);
                userModel.setEmail(email);
                userModel.setPassword(password);
                userModel.setPhone(phone);
                userModel.setDocument(document);
                userModel.setAccountType(accountType);

                preparedStatement.close();
                resultSet.close();

                return userModel;

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean updatePassword(int id, String password) {
        String sql = "UPDATE user_model SET password = ?" +
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
}
