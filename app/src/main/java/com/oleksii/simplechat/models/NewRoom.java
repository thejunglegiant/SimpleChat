package com.oleksii.simplechat.models;

import java.util.ArrayList;
import java.util.LinkedList;

public class NewRoom {

    private String title;
    private LinkedList<User> users;


    public NewRoom(String title, LinkedList<User> users) {
        this.title = title;
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public LinkedList<User> getUsers() {
        return users;
    }
}
