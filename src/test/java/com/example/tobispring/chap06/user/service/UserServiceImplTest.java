package com.example.tobispring.chap06.user.service;

import static com.example.tobispring.chap06.user.domain.Level.GOLD;
import static com.example.tobispring.chap06.user.domain.Level.SILVER;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tobispring.chap06.AppConfigChap06;
import com.example.tobispring.chap06.user.aop.TransactionHandler;
import com.example.tobispring.chap06.user.dao.UserDao;
import com.example.tobispring.chap06.user.domain.Level;
import com.example.tobispring.chap06.user.domain.User;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootTest(classes = AppConfigChap06.class)
class UserServiceImplTest {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("idA", "nameA", "passwordA", Level.BASIC, 49, 0, "emailA"),
                new User("idB", "nameB", "passwordB", Level.BASIC, 50, 0, "emailB"), // 대상
                new User("idC", "nameC", "passwordC", SILVER, 60, 29, "emailC"),
                new User("idD", "nameD", "passwordD", SILVER, 60, 30, "emailD"), // 대상
                new User("idE", "nameE", "passwordE", GOLD, 100, 100, "emailE"));
    }

    @Test
    void add() {
        // given
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null); // 레벨 초기화

        // when
        userServiceImpl.add(userWithLevel);
        userServiceImpl.add(userWithoutLevel);

        // then
        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel());
        assertEquals(userWithoutLevelRead.getLevel(), Level.BASIC);
    }

    /*@Test 리팩토링 전 upgradeLevels()
    void upgradeLevels() {
        // given
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        // when
        userServiceImpl.upgradeLevels();

        // then
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }*/

    /*@Test // 목 클래스 생성 후 upgradeLevels()
    void upgradeLevels() {
        // given
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        MockUserDao mockUserDao = new MockUserDao(users);
        userServiceImpl.setUserDao(mockUserDao);

        // when
        userServiceImpl.upgradeLevels();
        List<User> updated = mockUserDao.getUpdated();

        // then
        assertEquals(updated.size(), 2);
        checkUserAndLevel(updated.get(0), "idB", Level.SILVER);
        checkUserAndLevel(updated.get(1), "idD", Level.GOLD);
    }*/

    @Test // Mockito를 적용한 upgradeLevels()
    void mockUpgradeLevels() {
        // given
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(users);
        userServiceImpl.setUserDao(mockUserDao);

        // when
        userServiceImpl.upgradeLevels();

        // then
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class)); // update( ) 메서드의 파라미터로 User 클래스 어떤것이든 상관없다.
        verify(mockUserDao).update(users.get(1)); // update( ) 메서드의 파라미터로 users.get(1) 에 해당하는 파라미터여야 한다.
        assertEquals(users.get(1).getLevel(), SILVER);
        verify(mockUserDao).update(users.get(3)); // update( ) 메서드의 파라미터로 users.get(3) 에 해당하는 파라미터여야 한다.
        assertEquals(users.get(3).getLevel(), GOLD);
    }

    /*private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertEquals(updated.getId(), expectedId);
        assertEquals(updated.getLevel(), expectedLevel);
    }*/

    @Test
    void upgradeAllOrNothing() {
        // given
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);

        /*UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(transactionManager);
        txUserService.setUserService(testUserService);*/

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern("upgradeLevels");

        UserService txUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserService.class},
                txHandler);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        // when
        try {
            txUserService.upgradeLevels();
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

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void update(User updateUser) {
            updated.add(updateUser);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }

    static class TestUserService extends UserServiceImpl {
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

    static class TestUserServiceImpl extends UserServiceImpl {
        private String id;

        private TestUserServiceImpl(String id) {
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