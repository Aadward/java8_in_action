package com.syh.mybatis_demo.mapper;

import com.syh.mybatis_demo.po.Follower;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FollowerMapper {

  @Select("SELECT * FROM follower")
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "name", property = "name"),
      @Result(column = "user_id", property = "userId")
  })
  List<Follower> getall();
}
