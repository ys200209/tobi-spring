package com.example.tobispring.chap06.user.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum Level {
    GOLD(3, null, loginStatus -> false),
    SILVER(2, GOLD, LevelUpgradeStatus::isOverUpgradeRecommendCount),
    BASIC(1, SILVER, LevelUpgradeStatus::isOverUpgradeLoginCount);

    private final int value;
    private final Level next;
    private final Function<LevelUpgradeStatus, Boolean> function;

    Level(int value, Level next, Function<LevelUpgradeStatus, Boolean> function) {
        this.value = value;
        this.next = next;
        this.function = function;
    }

    public int intValue() {
        return value;
    }

    public static Level valueOf(int value) {
        return Arrays.stream(values())
                .filter(level -> level.value == value)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Unknown value: " + value));
    }

    public Level nextLevel() {
        return this.next;
    }

    public boolean isNextLevelNull() {
        if (this.next == null) {
            return true;
        }
        return false;
    }

    public boolean canUpgradeLevel(LevelUpgradeStatus levelUpgradeStatus) {
        return Arrays.stream(values())
                .filter(level -> this == level)
                .map(level -> level.function.apply(levelUpgradeStatus))
                .findFirst()
                .orElseThrow(() -> new AssertionError("존재하지 않는 레벨입니다."));
    }
}
