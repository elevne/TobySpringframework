package org.example.thirdchapter;

import org.example.firstchapter.ConnectionMaker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {

    private JdbcContext jdbcContext;

    public UserDao(ConnectionMaker connectionMaker) {
        this.jdbcContext = new JdbcContext(connectionMaker);
    }

    public void deleteAll() throws SQLException, ClassNotFoundException {
        this.jdbcContext.workWithStatementStrategy(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                        return connection.prepareStatement("DELETE FROM users");
                    }
                }
        );
    }
}
