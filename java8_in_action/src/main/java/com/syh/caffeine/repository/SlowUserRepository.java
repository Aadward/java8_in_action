package com.syh.caffeine.repository;

import java.util.concurrent.TimeUnit;

import com.syh.caffeine.model.User;
import org.springframework.stereotype.Component;

@Component
public class SlowUserRepository implements UserRepository {

    @Override
    public User getUserById(int id) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User(id, "user" + id);
    }
}
