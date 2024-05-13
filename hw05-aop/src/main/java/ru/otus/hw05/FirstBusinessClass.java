package ru.otus.hw05;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirstBusinessClass implements TestLoggingInterface {

    @Log
    @Override
    public void method(int a) {
        log.info("Вызван method. a = {}", a);
    }

    @Log
    @Override
    public void method(int a, int b) {
        log.info("Вызван method. a = {}, b = {}", a, b);
    }

    @Override
    public void method(int a, int b, int c) {
        log.info("Вызван method. a = {}, b = {}, c = {}", a, b, c);
    }

    @Log
    @Override
    public void method(int a, int b, int c, String d) {
        log.info("Вызван method. a = {}, b = {}, c = {}, d = {}", a, b, c, d);
    }

}
