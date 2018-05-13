package com.example.protobuf;

import java.io.IOException;

import static com.example.protobuf.protocol.UserProto.User;
import static com.example.protobuf.protocol.VersionProto.Version;

public class Main {

    public static void main(String[] args) throws IOException {
        User user =
                User.newBuilder()
                        .setId(1)
                        .setName("test")
                        .setPhoneNumber("12345678910")
                        .setEmail("test@test.com")
                        .addDescription("nothing")
                        .setVersion(Version.newBuilder()
                                .setVersion(1))
                        .build();
        byte[] protoAsBytes = user.toByteArray();

        // deserialize
        User dUser = User.parseFrom(protoAsBytes);
        System.out.println(dUser);
    }
}
