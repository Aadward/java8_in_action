package com.syh.guice.service;

import com.syh.guice.annotation.Remote;
import com.syh.guice.model.User;
import com.syh.guice.repository.UserRepository;

import javax.inject.Inject;

public class RemoteUserService implements UserService {

    UserRepository userRepository;

    @Inject
    public RemoteUserService(@Remote UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(long id) {
        return userRepository.getUserById(id);
    }
}
