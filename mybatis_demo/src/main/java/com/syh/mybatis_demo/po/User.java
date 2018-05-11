package com.syh.mybatis_demo.po;

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

    long userId;

    @NonNull
    String username;

    @NonNull
    String password;

    @NonNull
    Date createTime;
}
