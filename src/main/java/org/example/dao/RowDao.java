package org.example.dao;

import org.example.model.Row;
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RowDao {
    public Row save(Row row) {
        String query = "INSERT INTO user_counter (counter, version) VALUES (?, ?)";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, row.getCounter());
            statement.setLong(2, row.getVersion());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = Long.valueOf(generatedKeys.getObject(1, Integer.class));
                row.setUserId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create connection to the BD", e);
        }

        return row;
    }

    public Long findCounterByUserId(Long userId) {
        String query = "SELECT counter FROM user_counter WHERE user_id=?";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("counter");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create connection to the BD", e);
        }

        return -1L;
    }
}
