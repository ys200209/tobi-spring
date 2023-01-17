package com.example.tobispring.chap05.user.service;

import com.example.tobispring.chap05.user.dao.UserDao;
import com.example.tobispring.chap05.user.domain.Level;
import com.example.tobispring.chap05.user.domain.User;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class UserService {
    UserDao userDao;
    private PlatformTransactionManager transactionManager;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                upgradeLevel(user);
            }
            transactionManager.commit(status);
        } catch (RuntimeException ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }

    protected void upgradeLevel(User user) {
        if (canUpgradeLevel(user)) {
            upgradeLevelAndUpdateDao(user);
        }
    }

    private boolean canUpgradeLevel(User user) {
        return user.canUpgradeLevel();
    }

    private void upgradeLevelAndUpdateDao(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}
