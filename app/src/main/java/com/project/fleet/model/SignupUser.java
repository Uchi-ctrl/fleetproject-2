package com.project.fleet.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SignupUser {
    private String companyName;
    private String about;
    private String mobile;
    private String email;
    private String password;

    public SignupUser() {
        // Default constructor required for calls to DataSnapshot.getValue(SignupUser.class)
    }

    public SignupUser(String companyName, String about, String mobile, String email, String password) {
        this.companyName = companyName;
        this.about = about;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAbout() {
        return about;
    }

    public String getMobile() {
        return mobile;
    }
}
