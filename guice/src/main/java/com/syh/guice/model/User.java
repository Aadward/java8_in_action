package com.syh.guice.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String address;

    private String postalCode;

    private String city;

    private String country;
}
