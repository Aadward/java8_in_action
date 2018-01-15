package com.syh.serialize.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Human implements Serializable {

    private static final long serialVersionUID = 4325090744297397139L;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private Sex sex;

    @Getter
    private List<Property> properties = new ArrayList<>();

    public boolean addProperty(Property property) {
        return properties.add(property);
    }

    public boolean removeProperty(Property property) {
        return properties.remove(property);
    }

}
