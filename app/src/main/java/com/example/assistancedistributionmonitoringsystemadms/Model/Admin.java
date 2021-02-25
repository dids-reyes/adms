package com.example.assistancedistributionmonitoringsystemadms.Model;

public class Admin {
    private String Name;
    private String Password;

    public Admin() {
    }

    public Admin(String name, String password) {
        Name = name;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
