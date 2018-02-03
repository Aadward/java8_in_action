package com.syh.guice.repository;

import com.syh.guice.model.User;

public class LocalUserRepositoryImpl implements UserRepository {

    @Override
    public User getUserById(long id) {
        return new User(1L, "FirstName", "LastName", "test@gmail.com",
                "Local", "0", "Chengdu", "China");
    }
}
