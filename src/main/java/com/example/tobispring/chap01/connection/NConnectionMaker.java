package com.example.tobispring.chap01.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class NConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        // N 커넥션 생성
        return null;
    }
}
