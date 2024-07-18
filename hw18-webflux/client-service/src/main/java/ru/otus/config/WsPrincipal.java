package ru.otus.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@Getter
@RequiredArgsConstructor
public class WsPrincipal implements Principal {

    private final String name;

}
