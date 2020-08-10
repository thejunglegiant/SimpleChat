package com.oleksii.simplechat.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Message {

    private String userId;
    private long roomId;
    @SerializedName("issender")
    private boolean isSender;
    private String firstname;
    private String lastname;
    private String body;
    @SerializedName("stime")
    private Timestamp sendingTime;

    public Message(String userId, long roomId, String body) {
        this.userId = userId;
        this.roomId = roomId;
        this.body = body;
    }

    public Message(String firstname, String body, Timestamp sendingTime) {
        this.firstname = firstname;
        this.body = body;
        this.sendingTime = sendingTime;
    }

    public Message(boolean isSender, String firstname, String lastname, String body, Timestamp stime) {
        this.isSender = isSender;
        this.firstname = firstname;
        this.lastname = lastname;
        this.body = body;
        this.sendingTime = stime;
    }

    public String getUserId() {
        return userId;
    }

    public long getRoomId() {
        return roomId;
    }

    public boolean isSender() {
        return isSender;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBody() {
        return body;
    }

    public Timestamp getSendingTime() {
        return sendingTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSendingTime(Timestamp sendingTime) {
        this.sendingTime = sendingTime;
    }
}
