package com.syh.jacksonusage.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Message {

    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    private LocalDate created;

    @JsonView(Views.Summary.class)
    private String title;

    @JsonView(Views.Summary.class)
    private User author;

    private List<User> recipients;

    private String body;
}
