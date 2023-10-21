package org.example.dao;


import org.example.model.Row;
import org.example.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OptimisticConcurrencyControlUpdate {
    public void updateAndIncrement(Row row) {
        String selectQuery = "SELECT * FROM user_counter WHERE user_id=?";
        String updateQuery = "UPDATE user_counter SET counter=?, version=? WHERE user_id=? AND version=?";
        int count = 0;

        try (Connection connection = ConnectionUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement statementSelect = connection.prepareStatement(selectQuery);
                    PreparedStatement statementUpdate = connection.prepareStatement(updateQuery)
            ) {
                while (true) {
                    statementSelect.setLong(1, row.getUserId());
                    ResultSet result = statementSelect.executeQuery();

                    if (result.next()) {
                        long counter = result.getLong("counter");
                        long version = result.getLong("version");

                        counter++;

                        statementUpdate.setLong(1, counter);
                        statementUpdate.setLong(2, version + 1);
                        statementUpdate.setLong(3, row.getUserId());
                        statementUpdate.setLong(4, version);

                        count = statementUpdate.executeUpdate();
                    }

                    if (count == 1) {
                        connection.commit();
                        break;
                    }
                    connection.rollback();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating and incrementing counter", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create connection to the DB", e);
        }
    }
}
