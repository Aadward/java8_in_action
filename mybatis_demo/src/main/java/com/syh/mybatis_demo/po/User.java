package com.syh.mybatis_demo.po;

import java.util.List;
import lombok.*;

import java.util.Date;
import org.apache.ibatis.type.Alias;

@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Getter
@Setter
@ToString
@Alias("user")
public class User {

    Long userId;

    @NonNull
    String username;

    @NonNull
    String password;

    @NonNull
    Date createTime;

    List<Follower> followers;
}
