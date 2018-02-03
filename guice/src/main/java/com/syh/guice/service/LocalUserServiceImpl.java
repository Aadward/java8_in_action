package com.syh.guice.service;

import com.syh.guice.annotation.Local;
import com.syh.guice.model.User;
import com.syh.guice.repository.UserRepository;

import javax.inject.Inject;

public class LocalUserServiceImpl implements UserService {

    UserRepository userRepository;

    @Inject
    public LocalUserServiceImpl(@Local UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(long id) {
        return userRepository.getUserById(id);
    }
}
