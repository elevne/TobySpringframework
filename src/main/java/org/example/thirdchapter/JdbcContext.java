package org.example.thirdchapter;

import org.example.firstchapter.ConnectionMaker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    private ConnectionMaker connectionMaker;

    public JdbcContext(ConnectionMaker connectionMaker) {this.connectionMaker = connectionMaker;}

    public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException, ClassNotFoundException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = this.connectionMaker.makeConnection();
            ps = strategy.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ps != null) {try {ps.close();} catch (SQLException e) {}}
            if (c != null) {try {c.close();} catch (SQLException e) {}}
        }
    }

}
