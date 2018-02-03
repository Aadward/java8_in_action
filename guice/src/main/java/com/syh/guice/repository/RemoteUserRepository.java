package com.syh.guice.repository;

import com.syh.guice.model.User;

public class RemoteUserRepository implements UserRepository {

    @Override
    public User getUserById(long id) {
        return new User(2L, "FirstName", "LastName", "test@gmail.com",
                "Remote", "0", "Chengdu", "China");
    }
}
