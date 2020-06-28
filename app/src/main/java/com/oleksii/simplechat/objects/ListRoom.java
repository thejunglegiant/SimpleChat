package com.oleksii.simplechat.objects;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class ListRoom {

    private String title;
    private String firstname;
    @SerializedName("last_message")
    private String lastMessage;
    @SerializedName("stime")
    private Timestamp lastActivity;
    private Boolean isAttached;

    public ListRoom(String title, String lastMessage, Timestamp lastActivity, Boolean isAttached) {
        this.title = title;
        this.lastMessage = lastMessage;
        this.lastActivity = lastActivity;
        this.isAttached = isAttached;
    }

    public ListRoom(String title, String lastMessage, Timestamp lastActivity) {
        this.title = title;
        this.lastMessage = lastMessage;
        this.lastActivity = lastActivity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastActivity(Timestamp lastActivity) {
        this.lastActivity = lastActivity;
    }

    public void setAttached(Boolean attached) {
        isAttached = attached;
    }

    public String getTitle() {
        return title;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Timestamp getLastActivity() {
        return lastActivity;
    }

    public Boolean getAttached() {
        return isAttached;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }
}
