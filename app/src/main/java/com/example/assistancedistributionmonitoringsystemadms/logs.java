package com.example.assistancedistributionmonitoringsystemadms;

public class logs {
    private String Attempt;
    private String Date;
    private String Email;
    private String Time;

    public logs() {
    }

    public logs(String attempt, String date, String email, String time) {
        Attempt = attempt;
        Date = date;
        Email = email;
        Time = time;
    }

    public String getAttempt() {
        return Attempt;
    }

    public void setAttempt(String attempt) {
        Attempt = attempt;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
