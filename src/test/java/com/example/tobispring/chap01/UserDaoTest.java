package com.example.tobispring.chap01;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class UserDaoTest {
    UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        userDao = new DaoFactory().userDao();
        userDao.deleteAll();
    }

    @RepeatedTest(10)
    void testA() throws SQLException {
        // when
        userDao.runMain();
    }

}