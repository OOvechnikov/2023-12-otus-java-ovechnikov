package ru.otus.services;

import lombok.RequiredArgsConstructor;
import ru.otus.dao.UserDao;

@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    @Override
    public boolean authenticate(String login, String password) {
        return userDao.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
