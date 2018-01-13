package com.syh.java8inaction.caffeine.spring;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.syh.java8inaction.caffeine.model.User;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    /**
     * 设置一个缓存Manager，如果缓存不存在则自动生成
     */
    @Bean("CaffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                .maximumSize(3) //最多缓存5个
                //weighter和maximumWeight结合起来用，当缓存对象的weight的和达到10000时，缓存达到上限,
                //不能和maxinumSize一起用
                //.weigher((Integer id, User user) -> id)
                //.maximumWeight(10000L)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .removalListener((Integer id, User user, RemovalCause cause) -> {})
                .recordStats();
        manager.setCaffeine(caffeine);

        return manager;
    }


    /**
     * 设置一个名为"user"的缓存
     */
    @Bean
    public CaffeineCache getUserCache() {
        com.github.benmanes.caffeine.cache.Cache caffeine = Caffeine.newBuilder()
                .maximumSize(3) //最多缓存5个
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .removalListener((Integer id, User user, RemovalCause cause) -> {})
                .recordStats()
                .build();
        return new CaffeineCache("user", caffeine);
    }

}
