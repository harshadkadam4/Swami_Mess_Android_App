package com.example.firebase_4;

// Used to ADD User

public class Users {

    String name,date,coming;

    public Users() {
    }

    public Users(String name, String date, String coming) {
        this.name = name;
        this.date = date;
        this.coming = coming;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComing() {
        return coming;
    }

    public void setComing(String coming) {
        this.coming = coming;
    }
}
