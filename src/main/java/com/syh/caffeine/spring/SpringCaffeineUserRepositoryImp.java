package com.syh.caffeine.spring;


import com.syh.caffeine.model.User;
import com.syh.caffeine.repository.SlowUserRepository;
import com.syh.caffeine.repository.UserRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@CacheConfig(cacheNames = "user", cacheManager = "CaffeineCacheManager")
@Component
public class SpringCaffeineUserRepositoryImp {

    private UserRepository slowRepository = new SlowUserRepository();

    @Cacheable(key = "#id")
    public User getUserById(int id) {
        return slowRepository.getUserById(id);
    }


    @CachePut(key = "#id")
    public User addUser(int id, User user) {
        //just add to cache
        return user;
    }


    @CacheEvict(allEntries = true)
    public void clearCache() {
        //no need to do anything
    }
}
