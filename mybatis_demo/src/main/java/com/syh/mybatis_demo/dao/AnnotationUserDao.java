package com.syh.mybatis_demo.dao;

import com.syh.mybatis_demo.mapper.AnnotationUserMapper;
import com.syh.mybatis_demo.po.User;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("annotation")
public class AnnotationUserDao implements UserDao {

    private final AnnotationUserMapper annotationUserMapper;

    @Autowired
    public AnnotationUserDao(AnnotationUserMapper annotationUserMapper) {
        this.annotationUserMapper = annotationUserMapper;
    }

    @Override
    public List<User> getAllUsers() {
        return annotationUserMapper.findAllUsers();
    }

    @Override
    public int addUser(User user) {
        return annotationUserMapper.insertUser(user);
    }

    @Override
    @Transactional
    public int addThenThrowException(User user) {
        annotationUserMapper.insertUser(user);
        throw new RuntimeException("error happens");
    }
}
