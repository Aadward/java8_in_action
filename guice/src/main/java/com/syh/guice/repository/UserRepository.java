package com.syh.guice.repository;

import com.syh.guice.model.User;

public interface UserRepository {

    User getUserById(long id);
}
