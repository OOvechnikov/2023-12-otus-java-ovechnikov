package ru.otus.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {

    private final long id;
    private final String name;
    private final String login;
    private final String password;

}
