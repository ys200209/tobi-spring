package com.example.tobispring.chap05.user.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void upgradeLevel() {
        // given
        Level[] levels = Level.values();
        for (Level level : levels) {
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
        Level[] levels = Level.values();
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