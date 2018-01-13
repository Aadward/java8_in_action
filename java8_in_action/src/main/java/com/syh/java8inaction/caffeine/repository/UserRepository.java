package com.syh.java8inaction.caffeine.repository;

import com.syh.java8inaction.caffeine.model.User;

public interface UserRepository {

    User getUserById(int id);
}
