package com.oleksii.simplechat.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = "saved_messages_table")
public class SavedMessage {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String roomTitle;
    @ColumnInfo(name = "is_sender")
    private boolean isSender;
    private String firstname;
    private String lastname;
    private String body;
    @ColumnInfo(name = "stime")
    private Date sendingTime;

    @Ignore
    public SavedMessage(boolean isSender, String body, Date sendingTime) {
        this.isSender = isSender;
        this.body = body;
        this.sendingTime = sendingTime;
    }

    public SavedMessage(boolean isSender, String firstname, String lastname, String body, Date sendingTime) {
        this.isSender = isSender;
        this.body = body;
        this.sendingTime = sendingTime;
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

    public Date getSendingTime() {
        return sendingTime;
    }

    public Timestamp getSendingTimestamp() {
        return new Timestamp(sendingTime.getTime());
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

    public void setSendingTime(Date sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
