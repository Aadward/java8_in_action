package com.syh.mybatis_demo.po;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Getter
@Setter
@ToString
public class User {

    long userId;

    @NonNull
    String username;

    @NonNull
    String password;

    @NonNull
    Date createTime;
}
