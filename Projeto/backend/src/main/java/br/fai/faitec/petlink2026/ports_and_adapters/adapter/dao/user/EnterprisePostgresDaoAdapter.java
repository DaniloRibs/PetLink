package br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user;

import br.fai.faitec.petlink2026.domain.user.EnterpriseModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.EnterpriseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnterprisePostgresDaoAdapter implements EnterpriseDao {

    private final Connection connection;

    public EnterprisePostgresDaoAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int add(EnterpriseModel entity) {
        final String userSql = "INSERT INTO user_model(email, full_name, password, phone) " +
                "VALUES(?,?,?,?);";

        final String enterpriseSql = "INSERT INTO enterprise_model(cnpj, user_id) VALUES(?,?);";

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

            PreparedStatement enterpriseStatement = connection.prepareStatement(enterpriseSql);
            enterpriseStatement.setString(1, entity.getCnpj());
            enterpriseStatement.setInt(2, userId);
            enterpriseStatement.execute();
            enterpriseStatement.close();

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
    public EnterpriseModel readyById(int id) {
        final String sql = "SELECT u.id, u.email, u.full_name, u.password, u.phone, " +
                "e.cnpj " +
                "FROM user_model u " +
                "JOIN enterprise_model e ON e.user_id = u.id " +
                "WHERE u.id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            EnterpriseModel enterpriseModel = null;

            if (resultSet.next()) {
                enterpriseModel = mapRow(resultSet);
            }

            resultSet.close();
            preparedStatement.close();

            return enterpriseModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EnterpriseModel> readAll() {
        final List<EnterpriseModel> entities = new ArrayList<>();

        final String sql = "SELECT u.id, u.email, u.full_name, u.password, u.phone, " +
                "e.cnpj " +
                "FROM user_model u " +
                "JOIN enterprise_model e ON e.user_id = u.id;";

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
    public void updateInformation(int id, EnterpriseModel entity) {
        final String userSql = "UPDATE user_model SET " +
                "full_name = ?, " +
                "email = ?, " +
                "phone = ? " +
                "WHERE id = ?;";

        final String enterpriseSql = "UPDATE enterprise_model SET cnpj = ? WHERE user_id = ?;";

        try {
            connection.setAutoCommit(false);

            PreparedStatement userStatement = connection.prepareStatement(userSql);
            userStatement.setString(1, entity.getFullname());
            userStatement.setString(2, entity.getEmail());
            userStatement.setString(3, entity.getPhone());
            userStatement.setInt(4, id);
            userStatement.executeUpdate();
            userStatement.close();

            PreparedStatement enterpriseStatement = connection.prepareStatement(enterpriseSql);
            enterpriseStatement.setString(1, entity.getCnpj());
            enterpriseStatement.setInt(2, id);
            enterpriseStatement.executeUpdate();
            enterpriseStatement.close();

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

    private EnterpriseModel mapRow(ResultSet resultSet) throws SQLException {
        final EnterpriseModel enterpriseModel = new EnterpriseModel();
        enterpriseModel.setId(resultSet.getInt("id"));
        enterpriseModel.setEmail(resultSet.getString("email"));
        enterpriseModel.setFullname(resultSet.getString("full_name"));
        enterpriseModel.setPassword(resultSet.getString("password"));
        enterpriseModel.setPhone(resultSet.getString("phone"));
        enterpriseModel.setCnpj(resultSet.getString("cnpj"));
        return enterpriseModel;
    }
}
