package com.example.tobispring.chap03.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TemplateTest {
    @DisplayName("중복 try-catch 제거 학습 테스트")
    @Test
    void testTemplate() {
        new UserDao(new UserDaoCall1()).basicMethod();
        new UserDao(new UserDaoCall2()).basicMethod();
    }

    static class UserDaoCall1 implements StrategyUserDao {
        @Override
        public void callMethod() {
            System.out.println("기능 1 시작");
        }
    }

    static class UserDaoCall2 implements StrategyUserDao {
        @Override
        public void callMethod() {
            System.out.println("기능 2 시작");
        }
    }

    interface StrategyUserDao {
        void callMethod();
    }

    static class UserDao {
        private final StrategyUserDao strategyUserDao;

        public UserDao(StrategyUserDao strategyUserDao) {
            this.strategyUserDao = strategyUserDao;
        }

        public void basicMethod() {
            try {
                strategyUserDao.callMethod();
            } catch (Exception e) {

            } finally {
                System.out.println("종료");
            }
        }
    }
}
