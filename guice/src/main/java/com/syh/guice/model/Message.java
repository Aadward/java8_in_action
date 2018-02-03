package com.syh.guice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {

    private Long id;

    private LocalDate created;

    private String title;

    private User author;

    private List<User> recipients;

    private String body;
}
