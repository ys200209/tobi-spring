package com.example.tobispring.chap06.user.domain;

import java.util.Objects;

public class User {

    private String id;
    private String name;
    private String password;
    private Level level;
    private LevelUpgradeStatus levelUpgradeStatus;

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.levelUpgradeStatus = new LevelUpgradeStatus(login, recommend);
    }

    public User() {
        this.levelUpgradeStatus = new LevelUpgradeStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public void setLogin(int login) {
        this.levelUpgradeStatus.setLogin(login);
    }

    public int getLogin() {
        return this.levelUpgradeStatus.getLogin();
    }

    public void setRecommend(int recommend) {
        this.levelUpgradeStatus.setRecommend(recommend);
    }

    public int getRecommend() {
        return this.levelUpgradeStatus.getRecommend();
    }

    public void upgradeLevel() {
        if (level.isNextLevelNull()) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        }
        level = level.nextLevel();
    }

    public boolean canUpgradeLevel() {
        return level.canUpgradeLevel(levelUpgradeStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name)
                && Objects.equals(password, user.password) && level == user.level && Objects.equals(
                levelUpgradeStatus, user.levelUpgradeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, level, levelUpgradeStatus);
    }
}
