package com.syh.jacksonusage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    private String firstname;

    @JsonView(Views.Summary.class)
    private String lastname;

    private String email;

    private String address;

    @JsonProperty("postal_code")
    private String postalCode;

    private String city;

    private String country;
}
