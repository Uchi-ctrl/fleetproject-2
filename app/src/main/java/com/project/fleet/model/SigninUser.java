package com.project.fleet.model;

public class SigninUser {
    private String email;
    private String password;

    //constructor will help to add data.
    public SigninUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
