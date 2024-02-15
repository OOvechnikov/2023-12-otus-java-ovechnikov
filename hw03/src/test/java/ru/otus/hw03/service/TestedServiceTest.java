package ru.otus.hw03.service;

import ru.otus.hw03_framework.annotation.After;
import ru.otus.hw03_framework.annotation.Before;
import ru.otus.hw03_framework.annotation.Test;
import ru.otus.hw03_framework.service.TestedService;

import java.util.Objects;

class TestedServiceTest {

    TestedService testedService = new TestedService();

    @Before
    void before() {
        System.out.println("Before");
    }

    @After
    void after() {
        System.out.println("After");
    }

    @Test
    void returnIntTest() {
        int returnInt = testedService.returnInt(5);
        Objects.equals(5, returnInt);
        System.out.println("Успешный тест");
    }

    @Test
    void throwExceptionTest() {
        System.out.println("Тест с ошибкой");
        testedService.throwException();
    }

}