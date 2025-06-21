package com.zdata.model;

import java.util.UUID;


public class Student {
    private UUID id;
    private String name;
    private String email;

    public Student(UUID id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId(){
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
