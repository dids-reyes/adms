package com.example.assistancedistributionmonitoringsystemadms;

public class S_and_G {
    String Name,Number,Email,Subsidy;
    S_and_G()
    {
        // undef constructor
    }
    public S_and_G(String Name, String Number, String Email, String Subsidy) {
        this.Name = Name;
        this.Number = Number;
        this.Email = Email;
        this.Subsidy = Subsidy;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String Number) {
        this.Number = Number;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getSubsidy() {
        return Subsidy;
    }

    public void setSubsidy(String Subsidy) {
        this.Subsidy = Subsidy;
    }

}
