package com.tech.socialworld.Model;

public class UserModel {
    private String name,email,password,profession;

    public UserModel(String name, String profession, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profession = profession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
