package ru.otus.hw01;

import com.google.common.collect.Collections2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class HelloOtus {

    @SuppressWarnings("java:S106")
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        Collection<Integer> transformed = Collections2.transform(list, i -> i = Objects.requireNonNull(i) + 100);
        System.out.println(transformed);
    }
}
