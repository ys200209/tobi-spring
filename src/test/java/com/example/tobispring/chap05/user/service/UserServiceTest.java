package com.example.tobispring.chap05.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.tobispring.chap05.AppConfigChap05;
import com.example.tobispring.chap05.user.dao.UserDao;
import com.example.tobispring.chap05.user.domain.Level;
import com.example.tobispring.chap05.user.domain.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AppConfigChap05.class)
class UserServiceTest {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("idA", "nameA", "passwordA", Level.BASIC, 49, 0),
                new User("idB", "nameB", "passwordB", Level.BASIC, 50, 0),
                new User("idC", "nameC", "passwordC", Level.SILVER, 60, 29),
                new User("idD", "nameD", "passwordD", Level.SILVER, 60, 30),
                new User("idE", "nameE", "passwordE", Level.GOLD, 100, 100));
    }

    @Test
    void upgradeLevels() {
        // given
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        // when
        userService.upgradeLevels();

        // then
        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    @Test
    void add() {
        // given
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null); // 레벨 초기화

        // when
        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        // then
        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel());
        assertEquals(userWithoutLevelRead.getLevel(), Level.BASIC);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User updateUser = userDao.get(user.getId());
        assertEquals(updateUser.getLevel(), expectedLevel);
    }
}