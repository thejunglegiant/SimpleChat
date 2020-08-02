package com.oleksii.simplechat.objects;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class User {

    @SerializedName("id")
    private String userId;

    private String firstname;

    private String lastname;

    @SerializedName("last_session")
    private Timestamp lastSession;

    public User(String userId, String firstname, String lastname) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String userId, String firstname, String lastname, Timestamp lastSession) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.lastSession = lastSession;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Timestamp getLastSession() {
        return lastSession;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLastSession(Timestamp session) {
        this.lastSession = session;
    }
}
