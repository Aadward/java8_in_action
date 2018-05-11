package com.syh.mybatis_demo;

import com.syh.mybatis_demo.dao.UserDao;
import com.syh.mybatis_demo.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MybatisDemoApplication implements CommandLineRunner {

    @Autowired
    private UserDao userDao;

    public static void main(String[] args) {
      SpringApplication.run(MybatisDemoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
	      User user = User.of("syh", "123", new Date());
        System.out.println(userDao.addUser(user));
        System.out.println("id=" + user.getUserId());
        System.out.println(userDao.getAllUsers());
    }
}
