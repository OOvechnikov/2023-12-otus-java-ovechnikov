package ru.otus;

import lombok.SneakyThrows;
import lombok.val;

import java.util.concurrent.Executors;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        int threadCount = 5;
        val resource = new Resource(10, threadCount);
        val executorService = Executors.newFixedThreadPool(threadCount);

        while (true) {
            executorService.submit(resource::use);
        }

    }

}
