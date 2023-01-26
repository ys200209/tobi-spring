package com.example.tobispring.chap06.user.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {
    com.example.tobispring.chap06.user.domain.User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void upgradeLevel() {
        // given
        com.example.tobispring.chap06.user.domain.Level[] levels = com.example.tobispring.chap06.user.domain.Level.values();
        for (com.example.tobispring.chap06.user.domain.Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }
            // when
            user.setLevel(level);
            user.upgradeLevel();

            // then
            assertEquals(user.getLevel(), level.nextLevel());
        }
    }

    @Test
    void cannotUpgradeLevel() {
        // given
        com.example.tobispring.chap06.user.domain.Level[] levels = com.example.tobispring.chap06.user.domain.Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null) {
                continue;
            }
            // when
            user.setLevel(level);

            // then
            assertThatThrownBy(() -> user.upgradeLevel())
                    .isInstanceOf(IllegalStateException.class);
        }
    }
}