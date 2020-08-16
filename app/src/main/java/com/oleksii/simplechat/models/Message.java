package com.oleksii.simplechat.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Message {

    private String userId;
    private long roomId;
    private String roomTitle;
    @SerializedName("issender")
    private boolean isSender;
    private String firstname;
    private String lastname;
    private String body;
    @SerializedName("stime")
    private Timestamp sendingTime;
    // 0 - just a regular Message, 1 - someone left group
    @SerializedName("viewtype")
    private int viewType;

    public Message(String userId, long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    public Message(String userId, long roomId, String body) {
        this.userId = userId;
        this.roomId = roomId;
        this.body = body;
    }

    public Message(String firstname, String lastname, Timestamp stime, int viewType) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.sendingTime = stime;
        this.viewType = viewType;
    }

    public Message(boolean isSender, String firstname, String lastname, String body, Timestamp stime,
                   int viewType) {
        this.isSender = isSender;
        this.firstname = firstname;
        this.lastname = lastname;
        this.body = body;
        this.sendingTime = stime;
        this.viewType = viewType;
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

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
