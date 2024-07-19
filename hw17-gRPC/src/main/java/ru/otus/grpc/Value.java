package ru.otus.grpc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Value {

    private final long firstValue;

    private final long currentValue;

    private final long lastValue;

}
