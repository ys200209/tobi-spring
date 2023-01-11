package com.example.tobispring.chap05.templatecallback;

public interface LineCallback<T> {
    T doSomethingWithLine (T value, String line);
}
