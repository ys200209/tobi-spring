package com.example.tobispring.chap04.learningtest.template;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
}
