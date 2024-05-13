package ru.otus.hw05;

import lombok.*;

public class Main {

    public static void main(String[] args) {
        val first = new FirstBusinessClass();
        val firstProxy = (TestLoggingInterface) Context.createProxy(first);
        firstProxy.method(2);
        firstProxy.method(2, 4);
        firstProxy.method(2, 4, 6);
        firstProxy.method(2, 4, 6, "a;sldkfjsd");

        System.out.println();

        val second = new SecondBusinessClass();
        val secondProxy = (TestLoggingInterface) Context.createProxy(second);
        secondProxy.method(20);
        secondProxy.method(20, 40);
        secondProxy.method(20, 40, 60);
        secondProxy.method(20, 40, 60, "String");

        System.out.println();

        val third = new ThirdBusinessClass();
        val thirdProxy = (SecondInterface) Context.createProxy(third);
        thirdProxy.method("sdgdfdf");
        thirdProxy.method(20, "fernenen");

        Context.createProxy("Should thrown exception.");
    }

}
