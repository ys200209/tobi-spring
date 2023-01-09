package com.example.tobispring.chap03.templatecallback;

public interface LineCallback<T> {
    T doSomethingWithLine (T value, String line);
}
