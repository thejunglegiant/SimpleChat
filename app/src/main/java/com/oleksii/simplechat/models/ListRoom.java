package com.oleksii.simplechat.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class ListRoom {

    private long id;
    private String title;
    private String firstname;
    @SerializedName("last_message")
    private String lastMessage;
    @SerializedName("stime")
    private Timestamp lastActivity;
    // For UI purposes
    private Boolean isAttached = false;
    private Boolean hasNewMessages = false;

    public ListRoom(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public ListRoom(String title, String lastMessage, Timestamp lastActivity, Boolean isAttached) {
        this.title = title;
        this.lastMessage = lastMessage;
        this.lastActivity = lastActivity;
        this.isAttached = isAttached;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getHasNewMessages() {
        return hasNewMessages;
    }

    public void setHasNewMessages(Boolean hasNewMessages) {
        this.hasNewMessages = hasNewMessages;
    }
}
