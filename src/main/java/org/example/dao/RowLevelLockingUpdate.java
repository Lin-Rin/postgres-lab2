package org.example.dao;

import org.example.model.Row;
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RowLevelLockingUpdate {
    public void updateAndIncrement(Row row) {
        String selectQuery = "SELECT counter FROM user_counter WHERE user_id = ? FOR UPDATE";
        String updateQuery = "UPDATE user_counter SET counter = ? WHERE user_id = ?";

        try (Connection connection = ConnectionUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

                selectStatement.setLong(1, row.getUserId());
                ResultSet selectResult = selectStatement.executeQuery();

                if (selectResult.next()) {
                    long counter = selectResult.getLong("counter");

                    updateStatement.setLong(1, ++counter);
                    updateStatement.setLong(2, row.getUserId());
                    updateStatement.executeUpdate();

                    connection.commit();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating counter", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create connection to the DB", e);
        }
    }
}
