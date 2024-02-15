package ru.otus.hw03_framework.service;

public class TestedService {

    public int returnInt(int testValue) {
        return testValue;
    }

    public void throwException() {
        throw new UnsupportedOperationException("Тут может быть ваше исключение");
    }

}