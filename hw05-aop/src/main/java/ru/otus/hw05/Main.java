package ru.otus.hw05;

import lombok.*;

public class Main {

    public static void main(String[] args) {
        val first = new FirstBusinessClass();
        var proxy = (TestLoggingInterface) Context.createProxy(first);
        proxy.method(2);
        proxy.method(2, 4);
        proxy.method(2, 4, 6);
        proxy.method(2, 4, 6, "a;sldkfjsd");

        System.out.println();

        val second = new SecondBusinessClass();
        proxy = (TestLoggingInterface) Context.createProxy(second);
        proxy.method(20);
        proxy.method(20, 40);
        proxy.method(20, 40, 60);
        proxy.method(20, 40, 60, "String");

        Context.createProxy("Should thrown exception.");
    }

}
