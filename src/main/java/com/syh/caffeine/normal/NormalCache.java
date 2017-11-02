package com.syh.caffeine.normal;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.syh.caffeine.model.User;
import com.syh.caffeine.repository.SlowUserRepository;
import com.syh.caffeine.repository.UserRepository;
import com.syh.util.StopWatch;

public class NormalCache {

    private UserRepository repository = new SlowUserRepository();

    private LoadingCache<Integer, User> cache;

    public NormalCache() {
        cache = Caffeine.newBuilder()
                .maximumSize(3) //最多缓存5个
                //weighter和maximumWeight结合起来用，当缓存对象的weight的和达到10000时，缓存达到上限,
                //不能和maxinumSize一起用
                //.weigher((Integer id, User user) -> id)
                //.maximumWeight(10000L)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .removalListener((Integer id, User user, RemovalCause cause) -> {})
                .recordStats()
                .build(id -> repository.getUserById(id)); //load缓存的方式
    }


    public User getUserById(int id) {
        return cache.get(id);
    }


    public static void main(String[] args) {
        StopWatch sw = new StopWatch();
        NormalCache normalCache = new NormalCache();

        //        Time used: 3013 MILLISECONDS
        //        Time used: 1 MILLISECONDS
        //        Time used: 3007 MILLISECONDS
        //        Time used: 4007 MILLISECONDS
        //        并不是达到上限才清楚缓存，当接近上限的时候就会开始清除，预留空间
        sw.start();
        IntStream.range(0, 3).boxed().forEach(i -> normalCache.getUserById(i));
        sw.stop();

        sw.start();
        IntStream.range(0, 3).boxed().forEach(i -> normalCache.getUserById(i));
        sw.stop();

        sw.start();
        IntStream.range(0, 6).boxed().forEach(i -> normalCache.getUserById(i));
        sw.stop();

        sw.start();
        IntStream.range(0, 6).boxed().forEach(i -> normalCache.getUserById(i));
        sw.stop();
    }
}
