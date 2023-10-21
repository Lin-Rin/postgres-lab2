package org.example.dao;

import org.example.model.Row;
import org.example.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LostUpdate {
    public void updateAndIncrement(Row row) {
        String selectQuery = "SELECT counter FROM user_counter WHERE user_id=?";
        String updateQuery = "UPDATE user_counter SET counter=? WHERE user_id=?";

        try (Connection connection = ConnectionUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement statementSelect = connection.prepareStatement(selectQuery);
                    PreparedStatement statementUpdate = connection.prepareStatement(updateQuery)
            ) {
                statementSelect.setLong(1, row.getUserId());
                statementUpdate.setLong(2, row.getUserId());
                ResultSet result = statementSelect.executeQuery();

                if (result.next()) {
                    long counter = result.getLong("counter");
                    statementUpdate.setLong(1, ++counter);
                    statementUpdate.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating and incrementing counter", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create connection to the DB", e);
        }
    }
}
