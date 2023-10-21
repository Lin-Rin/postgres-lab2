package org.example.dao;

import org.example.model.Row;
import org.example.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InPlaceUpdate {
    public void updateAndIncrement(Row row) {
        String updateQuery = "UPDATE user_counter SET counter=counter+1 WHERE user_id=?";

        try (Connection connection = ConnectionUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUpdate = connection.prepareStatement(updateQuery)) {
                statementUpdate.setLong(1, row.getUserId());
                statementUpdate.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating counter", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create connection to the DB", e);
        }
    }
}
