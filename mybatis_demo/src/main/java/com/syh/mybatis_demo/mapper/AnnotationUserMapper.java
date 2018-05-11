package com.syh.mybatis_demo.mapper;

import com.syh.mybatis_demo.po.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.List;

@Mapper
public interface AnnotationUserMapper {

    @Select("SELECT * FROM `user`")
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class)
    })
    List<User> findAllUsers();

    /**
     * Options是用来得到自动生成主键值的，而方法返回的int是指影响的行数
     * 主键值可以通过user.getUserId()得到
     */
    @Insert("INSERT INTO `user` (`username`, `password`, `create_time`) VALUES (#{user.username}, #{user.password}, #{user.createTime})")
    @Options(useGeneratedKeys = true, keyColumn = "user_id", keyProperty = "userId")
    int insertUser(@Param("user") User user);


    @Delete("DELETE FROM `user` WHERE user_id=#{userId}")
    int delete(@Param("user") User user);
}
