package com.example.tobispring.chap05.user.domain;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public enum Level {
    GOLD(3, null, (login, recommend) -> false),
    SILVER(2, GOLD, (login, recommend) -> recommend >= User.UPGRADE_RECOMMEND_COUNT_LOWER_BOUND),
    BASIC(1, SILVER, (login, recommend) -> login >= User.UPGRADE_LOGIN_COUNT_LOWER_BOUND);

    private final int value;
    private final Level next;
    private final BiFunction<Integer, Integer, Boolean> function;
//    private

    Level(int value, Level next, BiFunction<Integer, Integer, Boolean> function) {
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

    public boolean canUpgradeLevel(int login, int recommend) {
        return Arrays.stream(values())
                .filter(level -> this == level)
                .map(level -> level.function.apply(login, recommend))
                .findFirst()
                .orElseThrow(() -> new AssertionError("존재하지 않는 레벨입니다."));
    }
}
