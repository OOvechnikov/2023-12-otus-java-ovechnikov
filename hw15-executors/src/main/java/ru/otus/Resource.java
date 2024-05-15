package ru.otus;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Resource {

    private static final String THREAD_NAME = "pool-1-thread-";

    private final int max;
    private final int threadCount;
    private final Lock lock;
    private final Condition condition;

    private int count = 0;
    private int threadIncrement = 0;
    private boolean direction = true;

    public Resource(int max, int threadCount) {
        this.max = max;
        this.threadCount = threadCount;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    @SneakyThrows
    public void use() {
        try {
            lock.lock();
            while (!Thread.currentThread().getName().equals(THREAD_NAME + (threadIncrement + 1))) {
                condition.await();
            }
            System.out.printf("%s %d%n", Thread.currentThread().getName(), count);
            threadIncrement++;
            if (Thread.currentThread().getName().equals(THREAD_NAME + threadCount)) {
                increment();
                threadIncrement = 0;
            }
        } finally {
            condition.signalAll();
            lock.unlock();
        }
    }

    private void increment() {
        threadIncrement = 0;
        if (direction) {
            count++;
        } else {
            count--;
        }
        if (count == max || count == 0) {
            direction = !direction;
        }
    }

}
