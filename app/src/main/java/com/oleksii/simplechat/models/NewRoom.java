package com.oleksii.simplechat.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NewRoom {

    private String title;
    private List<User> users;


    public NewRoom(String title, List<User> users) {
        this.title = title;
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public List<User> getUsers() {
        return users;
    }
}
