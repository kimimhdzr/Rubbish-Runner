package com.example.trashrunner.DataBinding.Model;

public class CompanyBasicModel {
    private String id;
    private String name;
    private String state;
    private String email;

    public CompanyBasicModel(String id, String name, String state, String email) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getEmail() {
        return email;
    }
}

