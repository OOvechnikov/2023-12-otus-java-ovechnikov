package ru.otus.hw03;

import ru.otus.hw03_framework.framework.TestFramework;

public class Main {

    public static void main(String[] args) throws Exception {
        TestFramework testFramework = new TestFramework();
        testFramework.runTests("ru.otus.hw03.service.TestedServiceTest");
    }

}
