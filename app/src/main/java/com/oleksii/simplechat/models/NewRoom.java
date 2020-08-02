package com.oleksii.simplechat.models;

import java.util.ArrayList;

public class NewRoom {

    private String title;
    private ArrayList<User> users;


    public NewRoom(String title, ArrayList<User> users) {
        this.title = title;
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
