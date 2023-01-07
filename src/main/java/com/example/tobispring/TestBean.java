package com.example.tobispring;

public class TestBean {
    private int count = 0;

    public void increaseCount() {
        count++;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "count=" + count +
                '}';
    }
}
