package com.syh.mybatis_demo.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Getter
@Setter
@ToString
@Alias("follower")
public class Follower {

  private Long id;

  @NonNull
  private String name;

  @NonNull
  private Long userId;
}
