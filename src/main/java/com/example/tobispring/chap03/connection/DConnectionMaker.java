package com.example.tobispring.chap03.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        // D 커넥션 생성
        return null;
    }
}
