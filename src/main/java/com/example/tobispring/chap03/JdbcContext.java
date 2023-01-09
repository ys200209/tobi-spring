package com.example.tobispring.chap03;

import com.example.tobispring.chap03.strategy.StatementStrategy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ///////////////////////////////////
            ps = stmt.makePreparedStatement(c);
            ///////////////////////////////////

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public void executeSql(String query) throws SQLException {
        workWithStatementStrategy(
                c -> c.prepareStatement(query));
    }
}
