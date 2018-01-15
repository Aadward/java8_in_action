package com.syh.serialize.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
public class Property implements Serializable {

    private static final long serialVersionUID = -721420438330190860L;

    @Getter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private double price;
}
