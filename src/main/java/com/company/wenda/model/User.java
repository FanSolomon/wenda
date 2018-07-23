package com.company.wenda.model;

public class User {
    private String name;

    //Generate->Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name){
        this.name = name;
    }

    public String getDescription(){
        return "This is " + name;
    }
}
