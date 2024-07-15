package ru.otus.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Room {

    AGGREGATE_ROOM("1408");

    private final String criterion;

}