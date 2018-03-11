package com.syh.mybatis_demo.dao;

import com.syh.mybatis_demo.po.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    int addUser(User user);
}
