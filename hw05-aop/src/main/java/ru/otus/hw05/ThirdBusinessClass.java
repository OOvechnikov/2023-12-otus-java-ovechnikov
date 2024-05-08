package ru.otus.hw05;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThirdBusinessClass implements SecondInterface{

    @Override
    public void method(String a) {
        log.info("Вызван method. a = {}", a);
    }

    @Log
    @Override
    public void method(int a, String b) {
        log.info("Вызван method. a = {}, b = {}", a, b);
    }

}
