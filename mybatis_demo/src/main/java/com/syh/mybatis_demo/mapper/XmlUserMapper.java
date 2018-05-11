package com.syh.mybatis_demo.mapper;

import com.syh.mybatis_demo.po.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XmlUserMapper {

    List<User> findAllUsers();

    int insertUser(User user);

    int delete(User user);
}
