package com.oleksii.simplechat.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = "saved_messages_table")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("userid")
    private String userId;
    private long roomId;
    private String roomTitle;
    @ColumnInfo(name = "is_sender")
    @SerializedName("issender")
    private boolean isSender;
    private String firstname;
    private String lastname;
    private String body;
    @ColumnInfo(name = "stime")
    @SerializedName("stime")
    private Timestamp sendingTime;
    // 0 - just a regular Message, 1 - someone left group
    @ColumnInfo(name = "view_type")
    @SerializedName("viewtype")
    private int viewType;

    public Message() { }

    @Ignore
    public Message(String userId, long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    @Ignore
    public Message(boolean isSender, String userId, String body) {
        this.isSender = isSender;
        this.userId = userId;
        this.body = body;
        this.sendingTime = new Timestamp(new Date().getTime());
    }

    @Ignore
    public Message(String userId, long roomId, String body) {
        this.userId = userId;
        this.roomId = roomId;
        this.body = body;
    }

    @Ignore
    public Message(String firstname, String lastname, Timestamp stime, int viewType) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.sendingTime = stime;
        this.viewType = viewType;
    }

    @Ignore
    public Message(boolean isSender, String userId, String firstname, String lastname, String body,
                   Timestamp stime, int viewType) {
        this.isSender = isSender;
        this.userId = userId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

//package com.oleksii.simplechat.data.entities;
//
//        import androidx.room.ColumnInfo;
//        import androidx.room.Entity;
//        import androidx.room.Ignore;
//        import androidx.room.PrimaryKey;
//
//        import java.sql.Timestamp;
//        import java.util.Date;
//
//@Entity(tableName = "saved_messages_table")
//public class SavedMessage {
//
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//    private String roomTitle;
//    @ColumnInfo(name = "is_sender")
//    private boolean isSender;
//    private String firstname;
//    private String lastname;
//    private String body;
//    @ColumnInfo(name = "stime")
//    private Date sendingTime;
//
//    @Ignore
//    public SavedMessage(boolean isSender, String body, Date sendingTime) {
//        this.isSender = isSender;
//        this.body = body;
//        this.sendingTime = sendingTime;
//    }
//
//    public SavedMessage(boolean isSender, String firstname, String lastname, String body, Date sendingTime) {
//        this.isSender = isSender;
//        this.body = body;
//        this.sendingTime = sendingTime;
//    }
//
//    public boolean isSender() {
//        return isSender;
//    }
//
//    public String getFirstname() {
//        return firstname;
//    }
//
//    public String getLastname() {
//        return lastname;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public Date getSendingTime() {
//        return sendingTime;
//    }
//
//    public Timestamp getSendingTimestamp() {
//        return new Timestamp(sendingTime.getTime());
//    }
//
//    public void setSender(boolean sender) {
//        isSender = sender;
//    }
//
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//
//    public void setLastname(String lastname) {
//        this.lastname = lastname;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    public void setSendingTime(Date sendingTime) {
//        this.sendingTime = sendingTime;
//    }
//
//    public String getRoomTitle() {
//        return roomTitle;
//    }
//
//    public void setRoomTitle(String roomTitle) {
//        this.roomTitle = roomTitle;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//}

