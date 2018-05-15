package com.syh.mybatis_demo.dao;

import com.syh.mybatis_demo.mapper.XmlUserMapper;
import com.syh.mybatis_demo.po.User;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("xml")
public class XmlUserDao implements UserDao {

    private final XmlUserMapper xmlUserMapper;

    @Autowired
    public XmlUserDao(XmlUserMapper xmlUserMapper) {
        this.xmlUserMapper = xmlUserMapper;
    }


    @Override
    public List<User> getAllUsers() {
        return xmlUserMapper.findAllUsers();
    }

    @Override
    public int addUser(User user) {
        return xmlUserMapper.insertUser(user);
    }

    @Override
    @Transactional
    @Async
    public void addThenThrowException(User user) {
        xmlUserMapper.insertUser(user);
        throw new RuntimeException("error happens");
    }

}
