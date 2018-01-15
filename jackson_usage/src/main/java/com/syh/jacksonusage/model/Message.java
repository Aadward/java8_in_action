package com.syh.jacksonusage.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syh.jacksonusage.serializer.LocalDateDeserializer;
import com.syh.jacksonusage.serializer.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {

    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate created;

    @JsonView(Views.Summary.class)
    private String title;

    @JsonView(Views.Summary.class)
    private User author;

    private List<User> recipients;

    private String body;
}
