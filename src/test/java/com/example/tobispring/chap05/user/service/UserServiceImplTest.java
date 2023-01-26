package com.example.tobispring.chap05.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.tobispring.chap05.AppConfigChap05;
import com.example.tobispring.chap05.user.dao.UserDao;
import com.example.tobispring.chap05.user.domain.Level;
import com.example.tobispring.chap05.user.domain.User;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootTest(classes = AppConfigChap05.class)
class UserServiceImplTest {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("idA", "nameA", "passwordA", Level.BASIC, 49, 0),
                new User("idB", "nameB", "passwordB", Level.BASIC, 50, 0), // 대상
                new User("idC", "nameC", "passwordC", Level.SILVER, 60, 29),
                new User("idD", "nameD", "passwordD", Level.SILVER, 60, 30), // 대상
                new User("idE", "nameE", "passwordE", Level.GOLD, 100, 100));
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
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    void upgradeAllOrNothing() throws Exception {
        // given
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(transactionManager);
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        // when
        try {
            testUserService.upgradeLevels();
            Assertions.fail("TestUserServiceException expected");
        } catch (TestUserServiceException ex) {
        }

        // then

        checkLevelUpgraded(users.get(1), false);
    }

    // 리팩토링 전
    /*private void checkLevel(User user, Level expectedLevel) { // (유저, 다음 기대 레벨)
        User updateUser = userDao.get(user.getId());
        assertEquals(updateUser.getLevel(), expectedLevel);
    }*/

    // 리팩토링 후
    private void checkLevelUpgraded(User user, boolean upgraded) { // (유저, 다음으로 업그레이드 가능 여부)
        User updateUser = userDao.get(user.getId());
        if (upgraded) {
            assertEquals(updateUser.getLevel(), user.getLevel().nextLevel());
        } else {
            assertEquals(updateUser.getLevel(), user.getLevel());
        }
    }

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) { // id 를 가진 사용자가 들어오면 예외를 발생시킴
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }
}