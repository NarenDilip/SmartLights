package com.example.smartlights.smartapps.models;

public class Model {
    private String name;

    public Model(String name) {
        this.setName(name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
