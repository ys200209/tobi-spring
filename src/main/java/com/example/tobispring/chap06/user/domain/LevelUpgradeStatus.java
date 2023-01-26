package com.example.tobispring.chap06.user.domain;

import java.util.Objects;

public class LevelUpgradeStatus {
    public static final int UPGRADE_LOGIN_COUNT_LOWER_BOUND = 50;
    public static final int UPGRADE_RECOMMEND_COUNT_LOWER_BOUND = 30;

    private int login;
    private int recommend;

    public LevelUpgradeStatus() {
    }

    public LevelUpgradeStatus(int login, int recommend) {
        this.login = login;
        this.recommend = recommend;
    }

    public boolean isOverUpgradeLoginCount() {
        return login >= UPGRADE_LOGIN_COUNT_LOWER_BOUND;
    }

    public boolean isOverUpgradeRecommendCount() {
        return recommend >= UPGRADE_RECOMMEND_COUNT_LOWER_BOUND;
    }

    // 개판;
    public void setLogin(int login) {
        this.login = login;
    }
    // 개판;
    public int getLogin() {
        return login;
    }
    // 개판;
    public int getRecommend() {
        return recommend;
    }
    // 개판;
    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LevelUpgradeStatus that = (LevelUpgradeStatus) o;
        return login == that.login && recommend == that.recommend;
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, recommend);
    }
}
