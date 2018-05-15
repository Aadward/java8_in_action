package com.syh.mybatis_demo;

import com.syh.mybatis_demo.dao.UserDao;
import com.syh.mybatis_demo.mapper.FollowerMapper;
import com.syh.mybatis_demo.po.Follower;
import com.syh.mybatis_demo.po.User;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
public class MybatisDemoApplication implements CommandLineRunner {

    @Autowired
    private UserDao userDao;

    @Autowired
    private FollowerMapper followerMapper;

    public static void main(String[] args) {
      SpringApplication.run(MybatisDemoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
	      /*User user = User.of("syh", "123", new Date());
        System.out.println(userDao.addUser(user));
        System.out.println("id=" + user.getUserId());
        System.out.println(userDao.getAllUsers());

        TimeUnit.SECONDS.sleep(5);
        System.out.println(userDao.getAllUsers());*/

        System.out.println(userDao.getAllUsers());
        System.out.println(followerMapper.getall());
    }
}
