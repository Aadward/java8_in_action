package com.syh.caffeine.spring;

import java.util.stream.IntStream;

import com.syh.caffeine.model.User;
import com.syh.util.StopWatch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableCaching
public class SpringContext {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringContext.class);

        SpringCaffeineUserRepositoryImp repository = context.getBean(SpringCaffeineUserRepositoryImp.class);



        //        Time used: 3136 MILLISECONDS
        //        Time used: 3 MILLISECONDS
        //        Time used: 3003 MILLISECONDS
        //        Time used: 4013 MILLISECONDS

        StopWatch sw = new StopWatch();
        sw.start();
        IntStream.range(0, 3).boxed().forEach(i -> repository.getUserById(i));
        sw.stop();

        sw.start();
        IntStream.range(0, 3).boxed().forEach(i -> repository.getUserById(i));
        sw.stop();

        sw.start();
        IntStream.range(0, 6).boxed().forEach(i -> repository.getUserById(i));
        sw.stop();

        sw.start();
        IntStream.range(0, 6).boxed().forEach(i -> repository.getUserById(i));
        sw.stop();


        // ========================
        //        Time used: 2031 MILLISECONDS
        //        Time used: 2008 MILLISECONDS
        //        Time used: 3 MILLISECONDS
        System.out.println("===================");
        repository.clearCache();

        sw.start();
        IntStream.range(0, 2).boxed().forEach(i -> repository.getUserById(i));
        sw.stop();

        repository.clearCache();

        sw.start();
        IntStream.range(0, 2).boxed().forEach(i -> repository.getUserById(i));
        sw.stop();

        repository.clearCache();
        repository.addUser(0, new User(0,"user0"));
        repository.addUser(1, new User(1,"user1"));

        sw.start();
        IntStream.range(0, 2).boxed().forEach(i -> repository.getUserById(i));
        sw.stop();
    }
}
